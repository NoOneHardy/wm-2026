package ch.no1hardy.service.exception.user;

import ch.no1hardy.service.exception.BadRequestException;
import jakarta.validation.constraints.NotNull;

public class UserValidationException extends BadRequestException {
    public UserValidationException(@NotNull String field) {
        super(
                String.format("User validation failed for field: %s. Please check the input and try again.", field),
                String.format("Das Feld '%s' ist ungültig.", field)
        );
    }

    public UserValidationException(@NotNull String message, @NotNull String displayMessage) {
        super(message, displayMessage);
    }
}
