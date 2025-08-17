package ch.no1hardy.service.common;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class DateHelper {
    public static LocalDateTime getCurrentAbsoluteDate() {
        return LocalDateTime.now(ZoneId.of("CET"));
    }

    public static boolean isBeforeNow(LocalDateTime date) {
        return date.isBefore(getCurrentAbsoluteDate());
    }
}
