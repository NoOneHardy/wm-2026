package ch.no1hardy.service.user.presentation.rest;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import ch.no1hardy.service.user.application.port.in.UserRegistrationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRegistrationController {
    private final UserRegistrationUseCase useCase;

    @PostMapping("/users")
    public UserProfileDto registerUser(@RequestBody UserRegistrationDto dto) {
        return useCase.create(dto);
    }
}
