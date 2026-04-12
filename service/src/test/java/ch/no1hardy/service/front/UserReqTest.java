package ch.no1hardy.service.front;

import ch.no1hardy.service.exception.user.UserValidationException;
import ch.no1hardy.service.front.user.UserReq;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("validate() - GIVEN an empty username | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateEmptyUsername() {
        req.setUsername("");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("username must be at least 5 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a short username | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateShortUsername() {
        req.setUsername("m");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("username must be at least 5 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a long username | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateLongUsername() {
        req.setUsername("aVeryVeryVeryLongUsernameThatWillFailValidation");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("username can only contain 25 characters", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN an empty firstname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateEmptyFirstname() {
        req.setFirstname("");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("firstname must be at least 2 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a short firstname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateShortFirstname() {
        req.setFirstname("m");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("firstname must be at least 2 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a long firstname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateLongFirstname() {
        req.setFirstname("aVeryVeryVeryLongFirstnameThatWillFailValidation");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("firstname can only contain 25 characters", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN an empty lastname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateEmptyLastname() {
        req.setLastname("");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("lastname must be at least 2 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a short lastname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateShortLastname() {
        req.setLastname("m");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("lastname must be at least 2 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a long lastname | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateLongLastname() {
        req.setLastname("aVeryVeryVeryLongLastnameThatWillFailValidation");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("lastname can only contain 25 characters", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN an empty email | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateEmptyEmail() {
        req.setEmail("");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("email must be at least 5 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a short email | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateShortEmail() {
        req.setEmail("m");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("email must be at least 5 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a long email | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateLongEmail() {
        req.setEmail("aVeryVeryVeryLongEmailThatWillFailValidationButThisEmailIsEvenLongerBecauseItNeedsToBeLongerThan50Characters");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("email can only contain 50 characters", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN an empty password | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateEmptyPassword() {
        req.setPassword("");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("password must be at least 8 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a short password | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateShortPassword() {
        req.setPassword("m");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("password must be at least 8 characters long", e.getMessage());
    }

    @Test
    @DisplayName("validate() - GIVEN a long password | WHEN validating a dto | THEN a UserValidationException should be thrown")
    void validateLongPassword() {
        req.setPassword("aVeryVeryVeryLongPasswordThatWillFailValidationButThisPasswordIsEvenLongerBecauseItNeedsToBeLongerThan50Characters");

        var e = assertThrows(UserValidationException.class, () -> req.validate());
        assertEquals("password can only contain 50 characters", e.getMessage());
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

        req.setEmail("test@no1hardy.c1h");
        assertDoesNotThrow(req::validate);
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
