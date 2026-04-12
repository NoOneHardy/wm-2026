package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.preferences.NotificationPreferenceRes;
import ch.no1hardy.service.model.preferences.NotificationPreference;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS
)
public interface PreferencesMapper {
    NotificationPreferenceRes toDto(NotificationPreference entity);

    List<NotificationPreferenceRes> toDto(List<NotificationPreference> entities);
}
