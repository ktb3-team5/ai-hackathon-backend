package ktb.team5.ai.dto;

import java.util.List;

public record AiDescriptionRequest(String destName, List<String> destTags, List<String> userTags) {
}
