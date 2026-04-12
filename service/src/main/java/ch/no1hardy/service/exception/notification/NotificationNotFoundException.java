package ch.no1hardy.service.exception.notification;

import ch.no1hardy.service.exception.NotFoundException;
import jakarta.validation.constraints.NotNull;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException(@NotNull String id) {
        super(
                "Notification with id '" + id + "' not found.",
                "Benachrichtigung nicht gefunden."
        );
    }
}
