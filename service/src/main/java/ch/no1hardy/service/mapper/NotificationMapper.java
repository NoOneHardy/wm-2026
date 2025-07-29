package ch.no1hardy.service.mapper;

import ch.no1hardy.service.front.notification.NotificationRes;
import ch.no1hardy.service.model.notification.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS
)
public interface NotificationMapper {
    NotificationRes toDto(Notification entity);

    List<NotificationRes> toDto(List<Notification> entities);
}
