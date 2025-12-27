package ch.no1hardy.service.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateHelperTest {
    @Test
    @DisplayName("isBetween(LocalDateTime, LocalDateTime, LocalDateTime) - should return true if date is equal to start")
    void isBetween01() {
        LocalDateTime date = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 21, 12, 0);
        assertTrue(DateHelper.isBetween(date, start, end));
    }

    @Test
    @DisplayName("isBetween(LocalDateTime, LocalDateTime, LocalDateTime) - should return true if date is equal to end")
    void isBetween02() {
        LocalDateTime date = LocalDateTime.of(2025, 6, 21, 12, 0);
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 21, 12, 0);
        assertTrue(DateHelper.isBetween(date, start, end));
    }

    @Test
    @DisplayName("isBetween(LocalDateTime, LocalDateTime, LocalDateTime) - should return true if date is between start and end")
    void isBetween03() {
        LocalDateTime date = LocalDateTime.of(2025, 6, 20, 18, 0);
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 21, 12, 0);
        assertTrue(DateHelper.isBetween(date, start, end));
    }

    @Test
    @DisplayName("isBetween(LocalDateTime, LocalDateTime, LocalDateTime) - should return false if date is before start")
    void isBetween04() {
        LocalDateTime date = LocalDateTime.of(2025, 6, 20, 11, 59);
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 21, 12, 0);
        assertFalse(DateHelper.isBetween(date, start, end));
    }

    @Test
    @DisplayName("isBetween(LocalDateTime, LocalDateTime, LocalDateTime) - should return false if date is after end")
    void isBetween05() {
        LocalDateTime date = LocalDateTime.of(2025, 6, 21, 12, 1);
        LocalDateTime start = LocalDateTime.of(2025, 6, 20, 12, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 21, 12, 0);
        assertFalse(DateHelper.isBetween(date, start, end));
    }
}
