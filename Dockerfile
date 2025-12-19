# 1단계: 빌드 스테이지
FROM gradle:8.14-jdk21 AS builder

WORKDIR /app

# Gradle 래퍼 및 설정 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 실행 권한 부여
RUN chmod +x gradlew

# 소스 코드 복사
COPY src src

# 빌드 실행 (테스트 스킵)
RUN ./gradlew clean bootJar --no-daemon -x test

# 2단계: 실행 스테이지
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]