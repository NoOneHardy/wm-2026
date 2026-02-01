package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.domain.model.User;

public interface UserProfileMapper {
    UserProfileDto fromEntity(User user);
}
