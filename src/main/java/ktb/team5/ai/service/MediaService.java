package ktb.team5.ai.service;

import ktb.team5.ai.dto.MediaResponse;
import ktb.team5.ai.dto.MediaTopResponse;
import ktb.team5.ai.entity.Media;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.MediaRepository;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
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
        return mediaRepository.findAll(PageRequest.of(0, 10))
                .stream()
                .map(media -> MediaTopResponse.builder()
                        .id(media.getId())
                        .title(media.getTitle())
                        .type("Drama") // TODO: Media 엔티티에 type 필드 추가 필요
                        .posterUrl(getMediaPosterUrl(media))
                        .build())
                .toList();
    }

    private String getMediaPosterUrl(Media media) {
        // 1. posterUrl이 있으면 우선 사용
        if (media.getPosterUrl() != null && !media.getPosterUrl().isEmpty()) {
            String posterUrl = media.getPosterUrl();
            // /images/로 시작하면 /api를 앞에 붙여서 반환
            if (posterUrl.startsWith("/images/")) {
                return "/api" + posterUrl;
            }
            return posterUrl;
        }

        String basePath = "/api/images/media/";
        String resourceBasePath = "static/images/media/";
        String[] extensions = {".jpeg", ".jpg", ".png", ".webp", ".JPG", ".JPEG", ".PNG"};

        // 2. mediaUrl (한글 제목)이 있으면 해당 파일명으로 검색
        if (media.getMediaUrl() != null && !media.getMediaUrl().isEmpty()) {
            String mediaUrl = media.getMediaUrl();
            for (String ext : extensions) {
                String resourcePath = resourceBasePath + mediaUrl + ext;
                try {
                    ClassPathResource resource = new ClassPathResource(resourcePath);
                    if (resource.exists()) {
                        return basePath + mediaUrl + ext;
                    }
                } catch (Exception e) {
                    // 파일이 없으면 다음 확장자 시도
                }
            }
        }

        // 3. mediaUrl도 없으면 media ID 기반으로 검색
        Long id = media.getId();
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

        // 4. 파일을 찾지 못하면 1번 이미지를 기본값으로 사용
        return "/api/images/media/1.webp";
    }
}