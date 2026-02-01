package ch.no1hardy.service.user.infrastructure.persistence.mapper;

import ch.no1hardy.service.shared.UserId;
import ch.no1hardy.service.user.domain.model.Email;
import ch.no1hardy.service.user.domain.model.User;
import ch.no1hardy.service.user.infrastructure.persistence.jpa.UserJpaEntity;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface UserJpaMapper {
    @Mapping(target = "email", qualifiedByName = "mapToEmail")
    User toDomain(UserJpaEntity entity);

    @Mapping(source = "id.val", target = "id")
    @Mapping(source = "email.address", target = "email")
    UserJpaEntity toEntity(User user);

    @ObjectFactory
    default User createUser(UserJpaEntity entity) {
        return new User(new UserId(entity.getId()));
    }

    @Named("mapToUserId")
    default UserId mapToUserId(UUID id) {
        return new UserId(id);
    }

    @Named("mapToEmail")
    default Email mapToEmail(String email) {
        return new Email(email);
    }
}
