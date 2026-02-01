package ch.no1hardy.service.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequiredValidatorTest {
    private RequiredValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RequiredValidator();
    }

    @Test
    @DisplayName("isBlank(String) - should return true for null or blank strings")
    void isBlank01() {
        assertTrue(validator.isBlank(null));
        assertTrue(validator.isBlank(""));
        assertTrue(validator.isBlank("   "));
        assertFalse(validator.isBlank("a"));
        assertFalse(validator.isBlank("   a    "));
    }
}
