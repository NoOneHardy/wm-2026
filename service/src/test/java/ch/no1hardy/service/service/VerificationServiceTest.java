package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.verification.VerificationException;
import ch.no1hardy.service.front.verification.ResetPasswordReq;
import ch.no1hardy.service.front.verification.VerificationReq;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VerificationServiceTest {
    @Autowired
    private VerificationService verificationService;

    @MockitoSpyBean
    private VerificationCodeRepository verificationCodeRepository;

    @Test
    @DisplayName("getVerificationCode(String) - should call repository method")
    public void getVerificationCode01() {
        verificationService.getVerificationCode("test-code");
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).findByCode("test-code");
    }

    @Test
    @DisplayName("deleteVerificationCode(String) - should call repository method")
    public void deleteVerificationCode01() {
        Mockito.doReturn(Optional.of(new VerificationCode())).when(verificationCodeRepository).findByCode("test-code");
        verificationService.deleteVerificationCode("test-code");
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).delete(Mockito.any(VerificationCode.class));
    }

    @Test
    @DisplayName("deleteVerificationCode(String) - should not call repository delete method when code not found")
    public void deleteVerificationCode02() {
        Mockito.doReturn(Optional.empty()).when(verificationCodeRepository).findByCode("test-code");
        verificationService.deleteVerificationCode("test-code");
        Mockito.verify(verificationCodeRepository, Mockito.never()).delete(Mockito.any(VerificationCode.class));
    }

    @Test
    @DisplayName("verifyCode(VerificationCode, VerificationReq) - should return true for matching codes")
    public void verifyCode01() {
        VerificationCode code = new VerificationCode();
        VerificationReq req = new VerifyEmailReq(code.getCode());
        boolean result = verificationService.verifyCode(code, req);
        assertTrue(result);
    }

    @Test
    @DisplayName("verifyCode(VerificationCode, VerificationReq) - should return false for non-matching codes")
    public void verifyCode02() {
        VerificationCode code = new VerificationCode();
        VerificationReq req = new VerifyEmailReq("different-code");
        boolean result = verificationService.verifyCode(code, req);
        assertFalse(result);
    }

    @Test
    @DisplayName("saveVerificationCode(VerificationCode) - should call repository save method")
    public void saveVerificationCode01() {
        VerificationCode code = new VerificationCode();
        verificationService.saveVerificationCode(code);
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).save(code);
    }

    @Test
    @DisplayName("clearExpiredCodes() - should delete only expired codes")
    public void clearExpiredCodes01() {
        VerificationCode expiredCode = Mockito.mock(VerificationCode.class);
        Mockito.when(expiredCode.isExpired()).thenReturn(true);

        VerificationCode expiredCode2 = Mockito.mock(VerificationCode.class);
        Mockito.when(expiredCode2.isExpired()).thenReturn(true);

        VerificationCode validCode = Mockito.mock(VerificationCode.class);
        Mockito.when(validCode.isExpired()).thenReturn(false);

        Mockito.doReturn(List.of(expiredCode, validCode, expiredCode2)).when(verificationCodeRepository).findAll();

        verificationService.clearExpiredCodes();

        Mockito.verify(verificationCodeRepository, Mockito.times(1)).delete(expiredCode);
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).delete(expiredCode2);
        Mockito.verify(verificationCodeRepository, Mockito.never()).delete(validCode);
    }

    @Test
    @DisplayName("verifyCode(Optional<VerificationCode>, VerificationReq) - should return true for matching codes")
    public void verifyCode03() {
        VerificationCode code = new VerificationCode();
        VerificationReq req = new VerifyEmailReq(code.getCode());
        boolean result = verificationService.verifyCode(Optional.of(code), req);
        assertTrue(result);
    }

    @Test
    @DisplayName("verifyCode(Optional<VerificationCode>, VerificationReq) - should throw exception for empty optional")
    public void verifyCode04() {
        VerificationReq req = new VerifyEmailReq("some-code");
        VerificationException exception = assertThrows(VerificationException.class, () -> verificationService.verifyCode(Optional.empty(), req));
        assertEquals("Invalid verification code", exception.getMessage());
    }

    @Test
    @DisplayName("verifyCode(Optional<VerificationCode>, VerificationReq) - should throw exception for invalid code")
    public void verifyCode05() {
        VerificationCode code = new VerificationCode();
        VerificationReq req = new VerifyEmailReq("some-code");
        VerificationException exception = assertThrows(VerificationException.class, () -> verificationService.verifyCode(Optional.of(code), req));
        assertEquals("Invalid verification code", exception.getMessage());
    }

    @Test
    @DisplayName("verifyEmailCode(User, VerifyEmailReq) - should call verifyCode with user's most recent email verification code")
    public void verifyEmailCode01() {
        User user = Mockito.mock(User.class);
        VerificationCode code = new VerificationCode();
        Mockito.when(user.getMostRecentEmailVerificationCode()).thenReturn(Optional.of(code));
        VerifyEmailReq req = new VerifyEmailReq(code.getCode());
        assertTrue(verificationService.verifyEmailCode(user, req));
        Mockito.verify(user, Mockito.times(1)).getMostRecentEmailVerificationCode();
    }

    @Test
    @DisplayName("verifyPasswordResetCode(User, ResetPasswordReq) - should call verifyCode with user's most recent password reset code")
    public void verifyPasswordResetCode01() {
        User user = Mockito.mock(User.class);
        VerificationCode code = new VerificationCode();
        Mockito.when(user.getMostRecentPasswordResetCode()).thenReturn(Optional.of(code));
        ResetPasswordReq req = new ResetPasswordReq(code.getCode(), "newPassword");
        assertTrue(verificationService.verifyPasswordResetCode(user, req));
        Mockito.verify(user, Mockito.times(1)).getMostRecentPasswordResetCode();
    }
}
