package ch.no1hardy.service.front.verification;

public record VerifyEmailReq(
        String code
) implements VerificationReq {
}
