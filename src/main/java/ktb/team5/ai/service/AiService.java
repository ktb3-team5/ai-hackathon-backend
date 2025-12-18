package ktb.team5.ai.service;

import ktb.team5.ai.dto.AiDescriptionRequest;
import ktb.team5.ai.entity.Destination;
import ktb.team5.ai.entity.Media;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.prompt.AiDescriptionPromptFactory;
import ktb.team5.ai.repository.DestinationRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatModel chatModel;
    private final AiDescriptionPromptFactory promptFactory;
    private final DestinationRepository destinationRepository;
    private final UserRepository userRepository;

    public String generateDescription(String sessionId, Long destId) {
        Destination destination = destinationRepository.findById(destId).orElseThrow(() ->
                new IllegalArgumentException("Destination not found")
        );
        Media media = destination.getMedia();
        User user = userRepository.findBySessionId(sessionId).orElseThrow(() ->
                new IllegalArgumentException("User not found")
        );

        Prompt prompt = promptFactory.createPrompt(
                media.getTitle(),
                destination.getName(),
                destination.getTags(),
                user.getTags()
        );

        ChatResponse response = chatModel.call(prompt);
        return response.getResults().getFirst()
                .getOutput()
                .getText();
    }
}
