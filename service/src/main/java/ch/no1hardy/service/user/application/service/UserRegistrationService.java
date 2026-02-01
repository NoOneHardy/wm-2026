package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import ch.no1hardy.service.user.application.port.in.UserRegistrationUseCase;
import ch.no1hardy.service.user.application.port.out.PasswordHasher;
import ch.no1hardy.service.user.application.port.out.UserProfileMapper;
import ch.no1hardy.service.user.application.port.out.UserRegistrationMapper;
import ch.no1hardy.service.user.application.port.out.UserWriteRepository;
import ch.no1hardy.service.user.application.validation.UserRegistrationValidator;
import ch.no1hardy.service.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {
    private final UserRegistrationValidator registrationValidator;
    private final PasswordHasher passwordHasher;
    private final UserWriteRepository writeRepository;
    private final UserRegistrationMapper registrationMapper;
    private final UserProfileMapper profileMapper;

    @Override
    public UserProfileDto create(UserRegistrationDto dto) {
        registrationValidator.validate(dto);
        UserRegistrationDto hashedDto = hashedPassword(dto);
        User user = registrationMapper.fromDto(hashedDto);
        User savedUser = writeRepository.save(user);
        return profileMapper.fromEntity(savedUser);
    }

    private UserRegistrationDto hashedPassword(UserRegistrationDto dto) {
        String hashedPassword = passwordHasher.hash(dto.password());
        return new UserRegistrationDto(
                dto.username(),
                hashedPassword,
                dto.email(),
                dto.firstname(),
                dto.lastname()
        );
    }
}
