package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.user.domain.model.User;

import java.util.Optional;

public interface UserReadRepositoryPort {
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
