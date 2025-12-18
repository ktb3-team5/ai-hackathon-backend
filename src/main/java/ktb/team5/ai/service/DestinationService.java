package ktb.team5.ai.service;

import ktb.team5.ai.dto.DestinationsResponse;
import ktb.team5.ai.dto.NaverGeocodeResponse;
import ktb.team5.ai.entity.Destination;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.DestinationRepository;
import ktb.team5.ai.repository.MediaRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final AiClient aiClient;
    private final NaverMapsClient naverMapsClient;

    @Transactional
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

        // 구글 스트릿뷰 링크 추가
        destinations.forEach(destination -> {
            if (destination.getGoogleStreetViewUrl() == null
                    || destination.getGoogleStreetViewUrl().isEmpty()) {
                NaverGeocodeResponse response = naverMapsClient.geocode(destination.getAddress());
                if (response == null || response.getAddresses().isEmpty()) {
                    return;
                }

                String url = response.getAddresses().stream().findFirst()
                        .map(address -> "https://www.google.com/maps/@?api=1"
                                + "&map_action=pano"
                                + "&viewpoint=" + address.getY() + "," + address.getX())
                        .orElse(null);
                destination.setGoogleStreetViewUrl(url);
            }
        });

        return destinations.stream()
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription(),
                        destination.getGoogleStreetViewUrl()
                )).toList();
    }

    @Transactional
    public List<DestinationsResponse> getDestinations(Long mediaId) {
        List<Destination> destinations = destinationRepository.findByMediaId(mediaId);

        // 구글 스트릿뷰 링크 추가
        destinations.forEach(destination -> {
            if (destination.getGoogleStreetViewUrl() == null
                    || destination.getGoogleStreetViewUrl().isEmpty()) {
                NaverGeocodeResponse response = naverMapsClient.geocode(destination.getAddress());
                if (response == null || response.getAddresses().isEmpty()) {
                    return;
                }
                String url = response.getAddresses().stream().findFirst()
                        .map(address -> "https://www.google.com/maps/@?api=1"
                                + "&map_action=pano"
                                + "&viewpoint=" + address.getY() + "," + address.getX())
                        .orElse(null);
                destination.setGoogleStreetViewUrl(url);
            }
        });

        return destinations.stream()
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription(),
                        destination.getGoogleStreetViewUrl()
                )).toList();
    }
}
