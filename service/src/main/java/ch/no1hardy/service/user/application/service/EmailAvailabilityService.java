package ch.no1hardy.service.user.application.service;

import ch.no1hardy.service.user.application.port.out.UserReadRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailAvailabilityService {
    private final UserReadRepository readRepositoryPort;

    public boolean isAvailable(String email) {
        return readRepositoryPort.findByEmail(email).isEmpty();
    }
}
