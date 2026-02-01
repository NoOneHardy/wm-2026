package ch.no1hardy.service.user.infrastructure.persistence;

import ch.no1hardy.service.user.application.port.out.UserWriteRepository;
import ch.no1hardy.service.user.domain.model.User;
import ch.no1hardy.service.user.infrastructure.persistence.jpa.UserJpaEntity;
import ch.no1hardy.service.user.infrastructure.persistence.jpa.UserJpaRepository;
import ch.no1hardy.service.user.infrastructure.persistence.mapper.UserJpaMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteRepositoryAdapter implements UserWriteRepository {
    private final UserJpaRepository jpaRepository;
    private final UserJpaMapper userJpaMapper;

    @Override
    @Transactional
    public User save(User user) {
        UserJpaEntity entity = userJpaMapper.toEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return userJpaMapper.toDomain(savedEntity);
    }
}
