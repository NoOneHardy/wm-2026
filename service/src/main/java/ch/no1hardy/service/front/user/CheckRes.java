package ch.no1hardy.service.front.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckRes {
    private Boolean isUsernameAvailable;
    private Boolean isEmailAvailable;
}
