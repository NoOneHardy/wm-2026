package ch.no1hardy.service.user.application.port.in;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.application.dto.UserRegistrationDto;

public interface UserRegistrationUseCase {
    UserProfileDto create(UserRegistrationDto dto);
}
