package ch.no1hardy.service.user.application.port.out;

import ch.no1hardy.service.user.domain.model.User;

public interface UserWriteRepository {
    User save(User user);
}
