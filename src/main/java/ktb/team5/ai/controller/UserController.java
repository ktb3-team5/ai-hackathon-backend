package ktb.team5.ai.controller;

import jakarta.servlet.http.HttpServletRequest;
import ktb.team5.ai.dto.CreateUserTagsRequest;
import ktb.team5.ai.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(HttpServletRequest httpRequest) {
        String sessionId = httpRequest.getSession(true).getId(); // 기존 세션이 없으면 새로 생성
        userService.createUser(sessionId);
    }

    @PostMapping("/me/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserTags(
            HttpServletRequest httpRequest,
            @RequestBody CreateUserTagsRequest request
    ) {
        String sessionId = httpRequest.getSession(false).getId(); // 기존 세션이 없으면 null 반환
        userService.createUserTags(sessionId, request);
    }
}
