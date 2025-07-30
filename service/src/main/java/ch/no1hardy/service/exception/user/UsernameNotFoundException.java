package ch.no1hardy.service.exception.user;

import ch.no1hardy.service.common.StringHelper;
import ch.no1hardy.service.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;

public class UsernameNotFoundException extends NotFoundException {
    public UsernameNotFoundException(@NotNull String id) {
        super(
                "User '" + id + "' not found.",
                "Benutzer '" + StringHelper.getFormattedId(id) + "' nicht gefunden."
        );
    }
}
