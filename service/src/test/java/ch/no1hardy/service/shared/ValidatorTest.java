package ch.no1hardy.service.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {
    private BaseValidator<?> validator;

    @BeforeEach
    void setUp() {
        validator = new BaseValidator<>() {
            @Override
            protected void validate(Object object) {
                // No implementation needed for testing isBlank
            }
        };
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

    @Test
    @DisplayName("isNull(Object) - should return true for null objects")
    void isNull01() {
        assertTrue(validator.isNull(null));
        assertFalse(validator.isNull(new Object()));
    }
}
