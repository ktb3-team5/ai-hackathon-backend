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

        // 미디어에 등장한 여행지
        List<Destination> mediaDestinations =
                destinationRepository.findByMediaId(mediaId);

        // AI 추천 여행지
        List<Long> aiRecommendedDestinationIds =
                aiClient.recommendDestinations(userTags);

        // 결과 저장 (중복 방지 + 순서 유지)
        Set<Destination> result = new LinkedHashSet<>();

        // ----------------------
        // 1차: AI ∩ 미디어
        // ----------------------
        for (Destination dest : mediaDestinations) {
            if (aiRecommendedDestinationIds.contains(dest.getId())) {
                result.add(dest);
            }
            if (result.size() == 3) break;
        }

        // ----------------------
        // 2차 fallback: AI 추천 단독
        // ----------------------
        if (result.size() < 3) {
            for (Long destId : aiRecommendedDestinationIds) {
                Destination destination = destinationRepository.findById(destId)
                        .orElseThrow(() -> new IllegalStateException("Destination not found"));
                result.add(destination);
                if (result.size() == 3) break;
            }
        }

        // ----------------------
        // 3차 fallback: 미디어 여행지
        // ----------------------
        if (result.size() < 3) {
            for (Destination dest : mediaDestinations) {
                result.add(dest);
                if (result.size() == 3) break;
            }
        }

        return result.stream()
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription()
                )).toList();
    }

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
