package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import ch.no1hardy.service.user.domain.model.Email;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class EmailValidator extends BaseValidator<Email> {
    private final EmailAvailabilityService availabilityService;

    public void validate(Email email) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (isNull(email)) errors.add("email is required");
        else if (isBlank(email.address())) errors.add("email is required");
        else if (!validatePattern(email.address())) errors.add("invalid email format");
        else if (!availabilityService.isAvailable(email.address())) errors.add("email is already taken");

        if (errors.isEmpty()) return;
        throw new UserValidationException(String.join(", ", errors));
    }

    private boolean validatePattern(String email) {
        return Pattern.compile("^[A-z0-9-.]+@([A-z0-9-]+\\.)+[A-z-]{2,4}$").matcher(email).matches();
    }
}
