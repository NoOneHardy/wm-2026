package ch.no1hardy.service.user.infrastructure.persistence;

import ch.no1hardy.service.shared.UserId;
import ch.no1hardy.service.user.application.port.out.UserReadRepository;
import ch.no1hardy.service.user.domain.model.User;
import ch.no1hardy.service.user.infrastructure.persistence.jpa.UserJpaRepository;
import ch.no1hardy.service.user.infrastructure.persistence.mapper.UserJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserReadRepositoryAdapter implements UserReadRepository {
    private final UserJpaRepository jpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.val()).map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(userJpaMapper::toDomain);
    }
}
