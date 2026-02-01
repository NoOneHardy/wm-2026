package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import ch.no1hardy.service.user.domain.model.User;

public interface UserRegistrationMapper {
    User fromDto(UserRegistrationDto dto);
}
