package ch.no1hardy.service.front;

import ch.no1hardy.service.exception.user.UserValidationException;
import ch.no1hardy.service.front.user.UserReq;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserReqTest {
    @NotNull
    UserReq req = new UserReq();

    @BeforeEach
    void beforeEach() {
        req = new UserReq();
        req.setUsername("No1Hardy");
        req.setEmail("test@no1hardy.ch");
        req.setFirstname("Silas");
        req.setLastname("Hardegger");
        req.setPassword("test1234");
    }

    @Test
    @DisplayName("validate() - should validate user request with valid data")
    void shouldValidateUserRequestWithValidData() {
        assertDoesNotThrow(req::validate);
    }

    @Test
    @DisplayName("validate() - should validate user request with null values")
    void shouldValidateUserRequestWithNullValues() {
        req.setUsername(null);
        req.setEmail(null);
        req.setFirstname(null);
        req.setLastname(null);
        req.setPassword(null);

        assertDoesNotThrow(req::validate);
    }

    @Test
    @DisplayName("validate() - should validate user request with empty values")
    void shouldValidateUserRequestWithEmptyValues() {
        req.setUsername("");
        req.setEmail("");
        req.setFirstname("");
        req.setLastname("");
        req.setPassword("");

        assertDoesNotThrow(req::validate);
    }

    @Test
    @DisplayName("validate() - should validate email with invalid format")
    void shouldValidateEmailWithValidFormat() {
        req.setEmail("invalid-email-format");
        assertThrows(UserValidationException.class, req::validate);

        req.setEmail("test!@no1hardy-asdf1234.ch");
        assertThrows(UserValidationException.class, req::validate);

        req.setEmail("test123@no1hardy!.ch");
        assertThrows(UserValidationException.class, req::validate);

        req.setEmail("test@no1hardy.1");
        assertThrows(UserValidationException.class, req::validate);

        req.setEmail("test@no1hardy.comms");
        assertThrows(UserValidationException.class, req::validate);
    }

    @Test
    @DisplayName("validate() - should validate password with invalid length")
    void shouldValidatePasswordWithInvalidLength() {
        req.setPassword("short");
        assertThrows(UserValidationException.class, req::validate);

        req.setPassword("1234567");
        assertThrows(UserValidationException.class, req::validate);
    }

    @Test
    @DisplayName("validateAll() - should validate all fields of user request")
    void shouldValidateAllFieldsOfUserRequest() {
        assertDoesNotThrow(req::validateAll);
    }

    @Test
    @DisplayName("validateAll() - should throw UserValidationException for missing fields")
    void shouldThrowUserValidationExceptionForMissingFields() {
        req.setUsername(null);
        req.setEmail(null);
        req.setPassword(null);
        req.setFirstname(null);
        req.setLastname(null);
        assertThrows(UserValidationException.class, req::validateAll);
    }

    @Test
    @DisplayName("validateAll() - should throw UserValidationException for empty fields")
    void shouldThrowUserValidationExceptionForEmptyFields() {
        req.setUsername("");
        req.setEmail("");
        req.setPassword("");
        req.setFirstname("");
        req.setLastname("");
        assertThrows(UserValidationException.class, req::validateAll);
    }

    @Test
    @DisplayName("validateAll() - should throw UserValidationException for invalid fields")
    void shouldThrowUserValidationExceptionForInvalidFields() {
        req.setEmail("invalid-email-format");
        req.setPassword("1234");
        assertThrows(UserValidationException.class, req::validateAll);
    }

    @Test
    @DisplayName("validateRequired() - should validate required fields")
    void shouldValidateRequiredFields() {
        assertDoesNotThrow(req::validateRequired);
    }

    @Test
    @DisplayName("validateRequired() - should throw UserValidationException for missing required fields")
    void shouldThrowUserValidationExceptionForMissingRequiredFields() {
        req.setUsername(null);
        req.setEmail(null);
        req.setPassword(null);
        req.setFirstname(null);
        req.setLastname(null);
        assertThrows(UserValidationException.class, req::validateRequired);
    }

    @Test
    @DisplayName("validateRequired() - should throw UserValidationException for empty required fields")
    void shouldThrowUserValidationExceptionForEmptyRequiredFields() {
        req.setUsername("");
        req.setEmail("");
        req.setPassword("");
        req.setFirstname("");
        req.setLastname("");
        assertThrows(UserValidationException.class, req::validateRequired);
    }

    @Test
    @DisplayName("validateRequired() - should throw UserValidationException if only one field is missing")
    void shouldThrowUserValidationExceptionIfOnlyOneFieldIsMissing() {
        req.setUsername(null);
        assertThrows(UserValidationException.class, req::validateRequired);

        req.setUsername("No1Hardy");
        req.setEmail(null);
        assertThrows(UserValidationException.class, req::validateRequired);

        req.setEmail("test@no1hardy.ch");
        req.setPassword(null);
        assertThrows(UserValidationException.class, req::validateRequired);

        req.setPassword("test1234");
        req.setFirstname(null);
        assertThrows(UserValidationException.class, req::validateRequired);

        req.setFirstname("Silas");
        req.setLastname(null);
        assertThrows(UserValidationException.class, req::validateRequired);

        req.setLastname("Hardegger");
        assertDoesNotThrow(req::validateRequired);
    }
}
