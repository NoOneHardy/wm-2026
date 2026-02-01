package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FirstnameValidator implements BaseValidator<String> {
    private final RequiredValidator requiredValidator;

    public void validate(String firstname) throws UserValidationException {
        List<String> errors = new ArrayList<>();
        if (requiredValidator.isBlank(firstname)) errors.add("firstname is required");

        if (errors.isEmpty()) return;
        throw new UserValidationException(String.join(", ", errors));
    }
}
