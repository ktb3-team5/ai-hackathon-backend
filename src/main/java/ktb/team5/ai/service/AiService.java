package ktb.team5.ai.service;

import ktb.team5.ai.dto.AiDescriptionRequest;
import ktb.team5.ai.prompt.AiDescriptionPromptFactory;
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

    public String generateDescription(AiDescriptionRequest request) {
        Prompt prompt = promptFactory.createPrompt(
                request.destName(),
                request.destTags(),
                request.userTags()
        );

        ChatResponse response = chatModel.call(prompt);
        return response.getResults().getFirst()
                .getOutput()
                .getText();
    }
}
