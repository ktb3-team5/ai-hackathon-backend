package ktb.team5.ai.service;

import ktb.team5.ai.dto.MediaResponse;
import ktb.team5.ai.dto.MediaTopResponse;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.MediaRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final AiClient aiClient;

    public List<MediaTopResponse> getTop10Media() {

        return List.of(
                MediaTopResponse.builder()
                        .id(1L)
                        .title("Squid Game")
                        .type("Drama")
                        .posterUrl("https://example.com/poster1.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(2L)
                        .title("The Glory")
                        .type("Drama")
                        .posterUrl("https://example.com/poster2.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(3L)
                        .title("Itaewon Class")
                        .type("Drama")
                        .posterUrl("https://example.com/poster3.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(4L)
                        .title("Kingdom")
                        .type("Drama")
                        .posterUrl("https://example.com/poster1.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(5L)
                        .title("D.P.")
                        .type("Drama")
                        .posterUrl("https://example.com/poster2.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(6L)
                        .title("My Name")
                        .type("Drama")
                        .posterUrl("https://example.com/poster3.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(7L)
                        .title("All of Us Are Dead")
                        .type("Drama")
                        .posterUrl("https://example.com/poster1.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(8L)
                        .title("Parasite")
                        .type("Movie")
                        .posterUrl("https://example.com/poster2.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(9L)
                        .title("12.12: The Day")
                        .type("Movie")
                        .posterUrl("https://example.com/poster3.jpg")
                        .build(),

                MediaTopResponse.builder()
                        .id(10L)
                        .title("Concrete Utopia")
                        .type("Movie")
                        .posterUrl("https://example.com/poster3.jpg")
                        .build()
        );
    }
}