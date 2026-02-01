package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PasswordValidator implements BaseValidator<String> {
    private final RequiredValidator requiredValidator;

    public void validate(String password) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (requiredValidator.isBlank(password)) errors.add("password is required");

        if (errors.isEmpty()) return;
        throw new UserValidationException(String.join(", ", errors));
    }
}
