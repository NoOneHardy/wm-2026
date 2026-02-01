package ch.no1hardy.service.user.presentation.mapper;

import ch.no1hardy.service.user.application.dto.UserRegistrationDto;
import ch.no1hardy.service.user.application.port.out.UserRegistrationMapper;
import ch.no1hardy.service.user.domain.model.Email;
import ch.no1hardy.service.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface UserRegistrationMapperImpl extends UserRegistrationMapper {
    @Mapping(target = "email", qualifiedByName = "mapEmail")
    User fromDto(UserRegistrationDto dto);

    @Named("mapEmail")
    default Email mapEmail(String email) {
        return new Email(email);
    }
}
