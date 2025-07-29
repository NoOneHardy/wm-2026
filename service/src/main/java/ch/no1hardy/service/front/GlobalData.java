package ch.no1hardy.service.front;

import ch.no1hardy.service.front.notification.NotificationRes;
import lombok.Builder;

import java.util.List;

@Builder
public record GlobalData(
        String id,
        List<NotificationRes> notifications
) {
}
