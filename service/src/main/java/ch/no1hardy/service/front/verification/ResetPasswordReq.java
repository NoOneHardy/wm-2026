package ch.no1hardy.service.front.verification;

public record ResetPasswordReq(
        String code,
        String newPassword
) implements VerificationReq {
}
