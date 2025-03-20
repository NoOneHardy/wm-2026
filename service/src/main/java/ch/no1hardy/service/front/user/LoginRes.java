package ch.no1hardy.service.front.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRes {
    private String token;
    private Long expiresIn;
}
