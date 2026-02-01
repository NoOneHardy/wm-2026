package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.shared.UserId;
import ch.no1hardy.service.user.domain.model.User;

import java.util.Optional;

public interface UserReadRepository {
    Optional<User> findById(UserId id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
