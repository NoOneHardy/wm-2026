package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.port.out.UserReadRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameAvailabilityService {
    private final UserReadRepositoryPort readRepositoryPort;

    public boolean isAvailable(String username) {
        return readRepositoryPort.findByUsername(username).isEmpty();
    }
}
