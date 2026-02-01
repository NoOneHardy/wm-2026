package ch.no1hardy.service.user.presentation.mapper;

import ch.no1hardy.service.user.application.dto.UserProfileDto;
import ch.no1hardy.service.user.application.port.out.UserProfileMapper;
import ch.no1hardy.service.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapperImpl extends UserProfileMapper {
    @Override
    UserProfileDto fromEntity(User user);
}
