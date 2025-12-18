package ktb.team5.ai.service;

import ktb.team5.ai.dto.DestinationsResponse;
import ktb.team5.ai.entity.Destination;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.DestinationRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final AiClient aiClient;

    public List<DestinationsResponse> getTop3RecommendedDestinations(
            String sessionId,
            Long mediaId
    ) {
        User user = userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<String> userTags = user.getTags();
        if (user.getTags() == null || user.getTags().isEmpty()) {
            return Collections.emptyList();
        }

        // AI 추천 여행지
        List<Long> destinationIds =
                aiClient.recommendDestinations(mediaId, userTags);

        List<Destination> destinations = destinationRepository.findByIdIn(destinationIds);
        if (destinations == null || destinations.isEmpty()) {
            return Collections.emptyList();
        }

        return destinations.stream()
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription()
                )).toList();
    }
}
