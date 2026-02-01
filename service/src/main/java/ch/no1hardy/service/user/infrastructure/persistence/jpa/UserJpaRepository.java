package ch.no1hardy.service.user.infrastructure.persistence.jpa;

import ch.no1hardy.service.shared.UserId;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends ListCrudRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);
    Optional<UserJpaEntity> findByUsername(String username);
}
