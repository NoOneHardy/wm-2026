package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.port.out.UserReadRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameAvailabilityService {
    private final UserReadRepository readRepositoryPort;

    public boolean isAvailable(String username) {
        return readRepositoryPort.findByUsername(username).isEmpty();
    }
}
