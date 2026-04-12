package ch.no1hardy.service.exception.user;

import ch.no1hardy.service.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;

public class UsernameNotFoundException extends NotFoundException {
    public UsernameNotFoundException(@NotNull String username) {
        super(
                "User '" + username + "' not found.",
                "Benutzer '" + username + "' nicht gefunden."
        );
    }
}
