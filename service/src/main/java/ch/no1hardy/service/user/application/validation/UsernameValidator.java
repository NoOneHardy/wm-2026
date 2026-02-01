package ch.no1hardy.service.user.application.validation;

import ch.no1hardy.service.shared.BaseValidator;
import ch.no1hardy.service.shared.RequiredValidator;
import ch.no1hardy.service.user.application.service.UsernameAvailabilityService;
import ch.no1hardy.service.user.domain.exception.UserValidationException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UsernameValidator implements BaseValidator<String> {
    private final UsernameAvailabilityService availabilityService;
    private final RequiredValidator requiredValidator;

    public void validate(String username) throws UserValidationException {
        List<String> errors = new ArrayList<>();
        if (requiredValidator.isBlank(username)) errors.add("username is required");
        else if (username.length() < 3) errors.add("username must be at least 3 characters long");
        else if (!availabilityService.isAvailable(username)) errors.add("username is already taken");

        if (errors.isEmpty()) return;
        throw new UserValidationException(String.join(", ", errors));
    }
}
