package ch.no1hardy.service.front.user;

import ch.no1hardy.service.model.user.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRes {
    private String id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Boolean isActive;
    private Integer points;
    private Integer lastReviewedPoints;
    private String avatarUrl;
    private Role role;
    private LocalDateTime confirmedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
