package ch.no1hardy.service.front.user;

import ch.no1hardy.service.model.user.Role;
import ch.no1hardy.service.model.user.UserApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserRes {
    private String id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Integer points;
    private Integer lastReviewedPoints;
    private String avatarUrl;
    private Role role;
    private LocalDateTime applicationReviewedAt;
    private UserApplicationStatus userApplicationStatus;
}
