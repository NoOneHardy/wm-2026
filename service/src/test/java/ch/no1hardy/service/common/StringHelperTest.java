package ch.no1hardy.service.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringHelperTest {
    @Test
    @DisplayName("getFormattedId() - should not modify id if shorter than 8 characters")
    void shouldNotFormatIfTooShort() {
        assertEquals("1234567", StringHelper.getFormattedId("1234567"));
    }

    @Test
    @DisplayName("getFormattedId() - should return last 7 characters if longer than 7")
    void shouldShortenId() {
        assertEquals("2345678", StringHelper.getFormattedId("12345678"));
    }
}
