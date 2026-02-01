package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegistrationValidator implements BaseValidator<User> {
    private final UsernameValidator usernameValidator;
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;
    private final FirstnameValidator firstnameValidator;
    private final LastnameValidator lastnameValidator;

    public void validate(User user) {
        usernameValidator.validate(user.getUsername());
        emailValidator.validate(user.getEmail());
        passwordValidator.validate(user.getPasswordHash());
        firstnameValidator.validate(user.getFirstname());
        lastnameValidator.validate(user.getLastname());
    }
}
