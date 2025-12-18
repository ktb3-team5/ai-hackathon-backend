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
            String mediaName,
            String destName,
            List<String> destTags,
            List<String> userTags
    ){
        return new Prompt(
                systemMessage(),
                userMessage(mediaName, destName, destTags, userTags)
        );
    }

    private SystemMessage systemMessage() {
        return new SystemMessage("""
        You are a professional travel storyteller specializing in Korean destinations.
        Your task is to connect the emotional experience of a Korean movie, drama, or show
        with the real-life experience of visiting a place.

        Highlight how the atmosphere, mood, or feeling conveyed by the media
        can be experienced again at the destination.
        
        Treat the media as an emotional reference only.
        Do NOT imply that events or stories from the media actually happened at the location.

        Persuade international travelers by evoking emotion, not by listing facts.
        Do NOT invent facts. Use only the provided information.
        Write in natural, fluent English.
        """);
    }

    private UserMessage userMessage(
            String mediaName,
            String destName,
            List<String> destTags,
            List<String> userTags
    ) {
        return new UserMessage("""
        Media title: %s
        Destination name: %s
        Destination keywords: %s
        User preference keywords: %s

        Using the emotional tone and atmosphere suggested by the media,
        describe how visiting this destination allows the traveler
        to relive or resonate with that feeling in real life.
        
        Keep the description grounded in real visitor experience.
        Avoid overly poetic or abstract expressions.

        Limit the response to a maximum of 30 words.
        Use no more than two sentences.
        """.formatted(
                mediaName,
                destName,
                String.join(", ", destTags),
                String.join(", ", userTags)
        ));
    }
}
