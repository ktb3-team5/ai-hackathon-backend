package ktb.team5.ai.dto;

import java.util.List;

public record AiRecommendRequest(Long mediaId, List<String> tags) {
}
