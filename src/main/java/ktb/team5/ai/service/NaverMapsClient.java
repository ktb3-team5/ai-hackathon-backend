package ktb.team5.ai.service;

import ktb.team5.ai.dto.NaverGeocodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverMapsClient {

    @Value("${naver.maps.key-id}")
    private String apiKeyId;

    @Value("${naver.maps.key}")
    private String apiKey;

    @Qualifier("naverWebClient")
    private final WebClient naverWebClient;

    public NaverGeocodeResponse geocode(String address) {
        return naverWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocode")
                        .queryParam("query", address)
                        .build(true))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Naver error body: {}", body))
                                .then(Mono.error(new RuntimeException("Naver API error")))
                )
                .bodyToMono(NaverGeocodeResponse.class)
                .block();
    }
}
