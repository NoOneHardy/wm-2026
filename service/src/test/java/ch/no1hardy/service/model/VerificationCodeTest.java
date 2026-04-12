package ch.no1hardy.service.model;

import ch.no1hardy.service.TestUtils;
import ch.no1hardy.service.common.DateHelper;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class VerificationCodeTest {
    VerificationCode code;

    @BeforeEach
    void beforeEach() {
        code = new VerificationCode();
        code.setId("code-0");
    }

    @Test
    @DisplayName("VerificationCode() - should generate a code upon creation")
    void VerificationCode01() {
        assertEquals(36, code.getCode().length());
    }

    @Test
    @DisplayName("VerificationCode() - should generate unique codes")
    void VerificationCode02() {
        VerificationCode anotherCode = new VerificationCode();
        anotherCode.setId("code-1");

        assertEquals(36, anotherCode.getCode().length());
        assertNotEquals(code.getCode(), anotherCode.getCode());
    }

    @Test
    @DisplayName("VerificationCode(VerificationCodeType, User) - should set default expiration time to 10 minutes")
    void VerificationCode04() {
        User user = new User();
        TestUtils.checkTime(() -> new VerificationCode(VerificationCodeType.EMAIL, user), VerificationCode::getExpiresAt, true, 10);
    }

    @Test
    @DisplayName("VerificationCode(VerificationCodeType, User) - should associate the code with the given user and type")
    void VerificationCode05() {
        User user = new User();
        this.code = new VerificationCode(VerificationCodeType.EMAIL, user);

        assertEquals(user, code.getUser());
        assertEquals(VerificationCodeType.EMAIL, code.getType());
    }

    @Test
    @DisplayName("VerificationCode(int, VerificationCodeType, User) - should set custom expiration time")
    void VerificationCode06() {
        User user = new User();
        int validMinutes = 15;
        LocalDateTime start = LocalDateTime.now();
        this.code = new VerificationCode(validMinutes, VerificationCodeType.EMAIL, user);
        LocalDateTime end = LocalDateTime.now();

        assertNotNull(code.getExpiresAt());
        assertTrue(DateHelper.isBetween(code.getExpiresAt(), start.plusMinutes(validMinutes), end.plusMinutes(validMinutes)));
    }

    @Test
    @DisplayName("VerificationCode(int, VerificationCodeType, User) - should associate the code with the given user and type")
    void VerificationCode07() {
        User user = new User();
        this.code = new VerificationCode(15, VerificationCodeType.EMAIL, user);

        assertEquals(user, code.getUser());
        assertEquals(VerificationCodeType.EMAIL, code.getType());
    }

    @Test
    @DisplayName("VerificationCode(LocalDateTime, VerificationCodeType, User) - should set exact expiration time")
    void VerificationCode08() {
        User user = new User();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);
        this.code = new VerificationCode(expiresAt, VerificationCodeType.EMAIL, user);

        assertEquals(expiresAt, code.getExpiresAt());
    }

    @Test
    @DisplayName("VerificationCode(LocalDateTime, VerificationCodeType, User) - should associate the code with the given user and type")
    void VerificationCode09() {
        User user = new User();
        this.code = new VerificationCode(LocalDateTime.now(), VerificationCodeType.EMAIL, user);

        assertEquals(user, code.getUser());
        assertEquals(VerificationCodeType.EMAIL, code.getType());
    }

    @Test
    @DisplayName("isExpired() - should return true if the code is expired")
    void isExpired01() {
        code.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        assertTrue(code.isExpired());
    }

    @Test
    @DisplayName("isExpired() - should return false if the code is not expired")
    void isExpired02() {
        code.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        assertFalse(code.isExpired());
    }

    @Test
    @DisplayName("isValid() - should return true if the code is not expired")
    void isValid01() {
        code.setExpiresAt(LocalDateTime.now().plusMinutes(1));
        assertTrue(code.isValid());
    }

    @Test
    @DisplayName("isValid() - should return false if the code is expired")
    void isValid02() {
        code.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        assertFalse(code.isValid());
    }
}
