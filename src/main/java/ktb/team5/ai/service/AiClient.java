package ktb.team5.ai.service;

import ktb.team5.ai.dto.MediaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiClient {

    public List<MediaResponse> recommendMedia(List<String> tags) {

        // 예: ["힐링", "자연", "도시"]
        // ↓
        // AI API 요청

        return List.of(
                new MediaResponse(/* 추천 결과 */)
        );
    }
}
