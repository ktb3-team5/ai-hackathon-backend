package ktb.team5.ai.service;

import ktb.team5.ai.dto.AiRecommendRequest;
import ktb.team5.ai.dto.AiRecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiClient {

    @Qualifier("aiWebClient")
    private final RestTemplate aiRestTemplate;

    public List<Long> recommendDestinations(Long mediaId, List<String> tags) {
        try {
            AiRecommendRequest request = new AiRecommendRequest(mediaId, tags);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AiRecommendRequest> entity = new HttpEntity<>(request, headers);

            log.info("Sending AI request: {}", request);

            ResponseEntity<AiRecommendResponse> response = aiRestTemplate.postForEntity(
                    "/api/recommendation/content-based",
                    entity,
                    AiRecommendResponse.class
            );

            log.info("Received AI response: {}", response.getBody());

            return response.getBody() != null && response.getBody().destinationIds() != null
                    ? response.getBody().destinationIds()
                    : List.of();

        } catch (Exception e) {
            log.error("AI recommendation failed", e);
            return List.of();
        }
    }
}