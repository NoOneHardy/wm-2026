package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.application.service.EmailAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import ch.no1hardy.service.user.domain.model.Email;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EmailValidator extends BaseValidator<Email> {
    private final EmailAvailabilityService availabilityService;

    public void validate(Email email) throws UserValidationException {
        List<String> errors = new ArrayList<>();

        if (isNull(email)) errors.add("email is required");
        else if (isBlank(email.address())) errors.add("email is required");
        else if (!availabilityService.isAvailable(email.address())) errors.add("email is already taken");

        throw new UserValidationException(String.join(", ", errors));
    }
}
