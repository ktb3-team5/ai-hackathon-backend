package ktb.team5.ai.service;

import ktb.team5.ai.dto.AiRecommendRequest;
import ktb.team5.ai.dto.AiRecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiClient {

    private final WebClient aiWebClient;

    public List<Long> recommendDestinations(Long mediaId, List<String> tags) {
        try {
            AiRecommendResponse response = aiWebClient.post()
                    .uri("/api/recommendation/content-based")
                    .bodyValue(new AiRecommendRequest(mediaId, tags))
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::isError,
                            clientResponse -> clientResponse
                                    .bodyToMono(String.class)
                                    .doOnNext(body ->
                                            log.error("AI server error: {}", body))
                                    .then(Mono.error(new RuntimeException("AI server error")))
                    )
                    .bodyToMono(AiRecommendResponse.class)
                    .block(Duration.ofSeconds(3));

            return response != null && response.destinationIds() != null
                    ? response.destinationIds()
                    : List.of();

        } catch (Exception e) {
            log.error("AI recommendation failed", e);
            return List.of();
        }
    }
}