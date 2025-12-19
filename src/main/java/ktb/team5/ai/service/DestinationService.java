package ktb.team5.ai.service;

import ktb.team5.ai.dto.DestinationsResponse;
import ktb.team5.ai.dto.NaverGeocodeResponse;
import ktb.team5.ai.entity.Destination;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.DestinationRepository;
import ktb.team5.ai.repository.MediaRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
            // 구글 스트리트뷰 URL이 없는 경우
            if (destination.getGoogleStreetViewUrl() == null
                    || destination.getGoogleStreetViewUrl().isEmpty()) {

                String targetAddress = destination.getKorAddress();
                log.warn("타겟 주소: [{}], target주소 NULL :[{}]", targetAddress,(targetAddress==null));
                NaverGeocodeResponse response = naverMapsClient.geocode(targetAddress);

                // [로그 추가 부분] 실패 조건 확인
                if (response == null || response.getAddresses().isEmpty()) {
                    log.warn("지오코딩 조회 실패 - 주소: [{}], Response Null 여부: [{}]",
                            targetAddress, (response == null));
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
                .limit(5)  // AI가 반환한 개수에 따라 3~5개 반환 (최대 5개)
                .map(destination -> new DestinationsResponse(
                        destination.getName(),
                        destination.getAddress(),
                        destination.getDescription(),
                        getImagePath(destination),
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
                        getImagePath(destination),
                        destination.getGoogleStreetViewUrl()
                )).toList();
    }

    private String getImagePath(Destination destination) {
        String basePath = "/api/images/destinations/";
        String resourceBasePath = "static/images/destinations/";
        String[] extensions = {".png", ".jpg", ".jpeg", ".PNG", ".JPG", ".JPEG"};

        // 1. imageUrl (한글 파일명)이 있으면 해당 파일 검색
        if (destination.getImageUrl() != null && !destination.getImageUrl().isEmpty()) {
            String imageUrl = destination.getImageUrl();

            // 이미 /api/images/로 시작하면 그대로 반환
            if (imageUrl.startsWith("/api/images/")) {
                return imageUrl;
            }

            // /images/로 시작하면 /api 붙여서 반환
            if (imageUrl.startsWith("/images/")) {
                return "/api" + imageUrl;
            }

            // 한글 파일명으로 실제 파일 검색
            for (String ext : extensions) {
                String resourcePath = resourceBasePath + imageUrl + ext;
                try {
                    ClassPathResource resource = new ClassPathResource(resourcePath);
                    if (resource.exists()) {
                        return basePath + imageUrl + ext;
                    }
                } catch (Exception e) {
                    // 파일이 없으면 다음 확장자 시도
                }
            }
        }

        // 2. imageUrl로 못 찾았으면 destination ID 기반으로 검색
        Long id = destination.getId();
        for (String ext : extensions) {
            String resourcePath = resourceBasePath + id + ext;
            try {
                ClassPathResource resource = new ClassPathResource(resourcePath);
                if (resource.exists()) {
                    return basePath + id + ext;
                }
            } catch (Exception e) {
                // 파일이 없으면 다음 확장자 시도
            }
        }

        // 3. 파일을 찾지 못하면 첫 번째 이미지를 기본값으로 사용
        return "/api/images/destinations/주문진영진해변방사제.png";
    }
}
