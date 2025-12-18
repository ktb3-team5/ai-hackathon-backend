package ktb.team5.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ktb.team5.ai.dto.DestinationsResponse;
import ktb.team5.ai.dto.MediaTopResponse;
import ktb.team5.ai.service.DestinationService;
import ktb.team5.ai.service.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@AllArgsConstructor
public class MediaController {

    private final MediaService mediaService;
    private final DestinationService destinationService;

    @GetMapping("/top10")
    public List<MediaTopResponse> getTop10() {
        return mediaService.getTop10Media();
    }

    @GetMapping("{mediaId}/destinations/top3")
    public List<DestinationsResponse> getTop3Destinations(
            @PathVariable Long mediaId,
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new IllegalStateException("Session not found");
        }

        String sessionId = session.getId();
        return destinationService.getTop3RecommendedDestinations(sessionId, mediaId);
    }

    @GetMapping("{mediaId}/destinations")
    public List<DestinationsResponse> getDestinations(
            @PathVariable Long mediaId,
            HttpServletRequest httpRequest
    ) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new IllegalStateException("Session not found");
        }

        return destinationService.getDestinations(mediaId);
    }
}