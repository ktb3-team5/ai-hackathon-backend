package ktb.team5.ai.service;

import ktb.team5.ai.dto.DestinationsResponse;
import ktb.team5.ai.entity.Destination;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.DestinationRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public List<DestinationsResponse> getTop5Destinations(String sessionId, Long mediaId) {
        User user = userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.getTags() == null || user.getTags().isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> userTagSet = new HashSet<>(user.getTags());

        List<Destination> destinations = destinationRepository.findByMediaId(mediaId);
        if (destinations == null || destinations.isEmpty()) {
            return Collections.emptyList();
        }

        List<Destination> sortedResult = destinations.stream()
                // 태그 교집합 개수 계산
                .sorted((d1, d2) -> {
                    int score1 = intersectionCount(userTagSet, d1.getTags());
                    int score2 = intersectionCount(userTagSet, d2.getTags());
                    return Integer.compare(score2, score1); // 내림차순
                })
                .limit(5).toList();

        return sortedResult.stream()
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription()
                )).toList();
    }

    private int intersectionCount(Set<String> userTags, List<String> destinationTags) {
        if (destinationTags == null || destinationTags.isEmpty()) {
            return 0;
        }

        return (int) destinationTags.stream()
                .filter(userTags::contains)
                .count();
    }
}
