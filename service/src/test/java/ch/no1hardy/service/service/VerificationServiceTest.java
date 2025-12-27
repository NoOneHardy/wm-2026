package ch.no1hardy.service.service;

import ch.no1hardy.service.TestUtils;
import ch.no1hardy.service.exception.user.UserNotFoundException;
import ch.no1hardy.service.exception.verification.VerificationException;
import ch.no1hardy.service.front.user.UserRes;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeRepository;
import ch.no1hardy.service.model.verification.VerificationCodeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VerificationServiceTest {
    @MockitoBean
    private UserService userService;

    @MockitoSpyBean
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private VerificationService service;

    @Test
    @DisplayName("confirmEmail(VerifyEmailReq) - should confirm email and return updated user")
    void confirmEmail01() {
        User user = new User();
        user.setId("user-1");
        when(userService.getRawOptional("user-1")).thenReturn(Optional.of(user));
        Mockito.doAnswer(invocation -> {
            user.setEmailConfirmedAt(LocalDateTime.now());
            return null;
        }).when(userService).confirmEmail(user);

        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
        when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

        VerifyEmailReq req = new VerifyEmailReq(code.getCode(), "user-1");

        TestUtils.checkTime(() -> {
            UserRes res = service.confirmEmail(req);
            assertEquals("user-1", res.getId());
        }, user::getEmailConfirmedAt);
    }

    @Test
    @DisplayName("confirmEmail(VerifyEmailReq) - should throw UserNotFoundException when user not found")
    void confirmEmail02() {
        try {
            service.confirmEmail(new VerifyEmailReq("some-code", "non-existing-user"));
        } catch (Exception e) {
            assertEquals(UserNotFoundException.class, e.getClass());
        }
    }

    @Test
    @DisplayName("confirmEmail(VerifyEmailReq) - should throw VerificationException when no code is found")
    void confirmEmail03() {
        try {
            User user = new User();
            user.setId("user-1");
            when(userService.getRawOptional("user-1")).thenReturn(Optional.of(user));

            service.confirmEmail(new VerifyEmailReq("invalid-code", "user-1"));
        } catch (Exception e) {
            assertEquals(VerificationException.class, e.getClass());
            assertEquals("Invalid verification code", e.getMessage());
        }
    }

    @Test
    @DisplayName("confirmEmail(VerifyEmailReq) - should delete verification code after confirming email")
    void confirmEmail04() {
        User user = new User();
        user.setId("user-1");
        when(userService.getRawOptional("user-1")).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userService).confirmEmail(user);

        VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
        when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

        VerifyEmailReq req = new VerifyEmailReq(code.getCode(), "user-1");
        service.confirmEmail(req);
        Mockito.verify(verificationCodeRepository, Mockito.times(1)).delete(code);
    }

    @Test
    @DisplayName("confirmEmail(VerifyEmailReq) - should throw VerificationException if code is invalid")
    void confirmEmail05() {
        try {
            User user = new User();
            user.setId("user-1");
            when(userService.getRawOptional("user-1")).thenReturn(Optional.of(user));
            Mockito.doNothing().when(userService).confirmEmail(user);

            VerificationCode code = user.createVerificationCode(VerificationCodeType.EMAIL, 10);
            when(verificationCodeRepository.findByCode(code.getCode())).thenReturn(Optional.of(code));

            VerifyEmailReq req = new VerifyEmailReq("ABC1234", "user-1");
            service.confirmEmail(req);
        } catch (Exception e) {
            assertEquals(VerificationException.class, e.getClass());
            assertEquals("Invalid verification code", e.getMessage());
        }
    }
}
