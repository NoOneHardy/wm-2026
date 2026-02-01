package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.user.domain.event.UserRegistered;

public interface UserEventBus {
    void publish(UserRegistered event);
}
