package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegistrationValidator implements BaseValidator<UserRegistrationDto> {
    private final UsernameValidator usernameValidator;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final FirstnameValidator firstnameValidator;
    private final LastnameValidator lastnameValidator;

    public void validate(UserRegistrationDto user) {
        usernameValidator.validate(user.username());
        emailValidator.validate(user.email());
        passwordValidator.validate(user.password());
        firstnameValidator.validate(user.firstname());
        lastnameValidator.validate(user.lastname());
    }
}
