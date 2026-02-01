package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRegistrationValidator extends BaseValidator<User> {
    private final UsernameValidator usernameValidator;
    private final EmailValidator emailValidator;

    public void validate(User user) {
        usernameValidator.validate(user.getUsername());
        emailValidator.validate(user.getEmail());
//        if (isBlank(user.getPasswordHash())) errors.add("password is required");

//        if (!errors.isEmpty()) throw new UserValidationException(String.join(", ", errors));
    }
}
