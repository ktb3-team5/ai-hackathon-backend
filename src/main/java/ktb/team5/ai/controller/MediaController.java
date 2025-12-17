package ktb.team5.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb.team5.ai.dto.MediaResponse;
import ktb.team5.ai.dto.MediaTopResponse;
import ktb.team5.ai.service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@AllArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/top10")
    public List<MediaTopResponse> getTop10() {
        return mediaService.getTop10Media();
    }

    @GetMapping
    public List<MediaResponse> getMedia(HttpServletRequest httpRequest) {
        String sessionId = httpRequest.getSession(false).getId();

        return mediaService.getMediaByTags(sessionId);
    }
}