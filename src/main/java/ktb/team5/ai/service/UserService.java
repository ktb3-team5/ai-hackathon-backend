package ktb.team5.ai.service;

import ktb.team5.ai.dto.CreateUserTagsRequest;
import ktb.team5.ai.entity.User;
import ktb.team5.ai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    public void createUser(String sessionId) {
        if (userRepository.findBySessionId(sessionId).isPresent()) {
            return;
        }

        User user = User.of(sessionId);
        userRepository.save(user);
    }

    public void createUserPreferences(String sessionId, CreateUserTagsRequest request) {
        User user = userRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setTags(request.tags());
    }
}