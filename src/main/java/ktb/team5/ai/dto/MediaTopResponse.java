package ktb.team5.ai.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MediaTopResponse {

    private Long id;
    private String title;
    private String type;
    private String posterUrl;
}