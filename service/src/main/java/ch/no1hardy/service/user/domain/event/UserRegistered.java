package ch.no1hardy.service.user.domain.event;

import ch.no1hardy.service.user.domain.model.User;

public record UserRegistered(
        User user
) {
}
