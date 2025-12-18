package ktb.team5.ai.prompt;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiDescriptionPromptFactory {

    public Prompt createPrompt(
            String destName,
            List<String> destTags,
            List<String> userTags
    ){
        return new Prompt(
                systemMessages(),
                userMessage(destName, destTags, userTags)
        );
    }

    private SystemMessage systemMessages() {
        return new SystemMessage("""
            You are a professional travel content writer specializing in Korean destinations.
            Write vivid, engaging descriptions that highlight Korean cultural context and emotional atmosphere.
            Your goal is to persuade international travelers to want to visit the place.
    
            Do NOT invent facts.
            Use only the information provided in the input.
            Write the response in natural, fluent English.
            """);
    }

    private UserMessage userMessage(
            String destName,
            List<String> destTags,
            List<String> userTags
    ) {
        return new UserMessage("""
        Place name: %s
        Place keywords: %s
        User preference keywords: %s

        Based on the information above,
        write a travel description in 1–2 sentences.
        """.formatted(
                destName,
                String.join(", ", destTags),
                String.join(", ", userTags)
        ));
    }

}
