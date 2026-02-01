package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import ch.no1hardy.service.user.application.port.in.UserRegistrationUseCase;
import ch.no1hardy.service.user.application.port.out.UserProfileMapper;
import ch.no1hardy.service.user.application.port.out.UserRegistrationMapper;
import ch.no1hardy.service.user.application.port.out.UserWriteRepository;
import ch.no1hardy.service.user.application.validation.UserRegistrationValidator;
import ch.no1hardy.service.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {
    private final UserRegistrationValidator registrationValidator;
    private final UserWriteRepository writeRepository;
    private final UserRegistrationMapper registrationMapper;
    private final UserProfileMapper profileMapper;

    @Override
    public UserProfileDto create(UserRegistrationDto dto) {
        registrationValidator.validate(dto);
        User user = registrationMapper.fromDto(dto);
        User savedUser = writeRepository.save(user);
        return profileMapper.fromEntity(savedUser);
    }
}
