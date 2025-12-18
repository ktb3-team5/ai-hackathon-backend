package ktb.team5.ai.service;

import ktb.team5.ai.dto.CreateUserTagsRequest;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(String sessionId) {
        if (userRepository.findBySessionId(sessionId).isPresent()) {
            return;
        }

        User user = User.of(sessionId);
        userRepository.save(user);
    }

    @Transactional
    public void createUserTags(String sessionId, CreateUserTagsRequest request) {
        User user = userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        List<String> userTags = List.of(request.gender(), request.ageGroup(), request.genre(), request.travelStyle());
        user.setTags(userTags);
    }
}