package ktb.team5.ai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("aiWebClient")
    public RestTemplate aiRestTemplate(
            RestTemplateBuilder builder,
            @Value("${ai.server.base-url:http://localhost:8000}") String baseUrl
    ) {
        return builder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @Qualifier("naverWebClient")
    public WebClient naverWebClient(
            @Value("${naver.maps.key-id}") String keyId,
            @Value("${naver.maps.key}") String key
    ) {
        return WebClient.builder()
                .baseUrl("https://maps.apigw.ntruss.com/map-geocode/v2")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", keyId)
                .defaultHeader("X-NCP-APIGW-API-KEY", key)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8")  // 추가
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))  // 버퍼 크기 증가
                .build();
    }
}

