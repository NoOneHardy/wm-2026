package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UsernameValidator extends BaseValidator<String> {
    private final UsernameAvailabilityService availabilityService;

    public void validate(String username) throws UserValidationException {
        List<String> errors = new ArrayList<>();
        if (isBlank(username)) errors.add("username is required");
        else if (!availabilityService.isAvailable(username)) errors.add("username is already taken");

        if (errors.isEmpty()) return;
        throw new UserValidationException(String.join(", ", errors));
    }
}
