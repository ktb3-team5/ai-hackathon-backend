# ============================================
# ai-hackathon-backend Dockerfile
# Spring Boot + Gradle + Java 21
# ============================================

# ----- Build Stage -----
FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app

# Gradle 파일 복사 (의존성 캐싱용)
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew ./
RUN chmod +x gradlew

# 소스 코드 복사
COPY src src

# 빌드 실행 (테스트 스킵)
RUN ./gradlew bootJar --no-daemon -x test


# ----- Run Stage -----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 보안: non-root 유저로 실행
RUN addgroup -g 1001 appgroup && adduser -u 1001 -G appgroup -D appuser
USER appuser

# JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# JVM 메모리 설정
# t3.micro (1GB RAM): -Xmx512m
# t3.small (2GB RAM): -Xmx1024m
# t3.medium (4GB RAM): -Xmx2048m
ENTRYPOINT ["java", "-Xmx512m", "-Dspring.profiles.active=prod", "-jar", "app.jar"]