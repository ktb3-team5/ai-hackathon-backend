package ktb.team5.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb.team5.ai.dto.AiDescriptionRequest;
import ktb.team5.ai.dto.AiDescriptionResponse;
import ktb.team5.ai.service.AiService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiDescriptionController {

    private final AiService aiService;

    @PostMapping("/descriptions")
    public AiDescriptionResponse generateDescription(
            HttpServletRequest httpRequest,
            @RequestBody AiDescriptionRequest request
    ) {
        String sessionId = httpRequest.getSession(false).getId();
        String description = aiService.generateDescription(sessionId, request.destId());
        return new AiDescriptionResponse(description);
    }
}
