package ktb.team5.ai.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "tags")
    private String tags;

    public static User of(String sessionId, String tags) {
        User user = new User();
        user.sessionId = sessionId;
        user.tags = tags;
        return user;
    }
}
