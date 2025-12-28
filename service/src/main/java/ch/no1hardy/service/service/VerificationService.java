package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.verification.VerificationException;
import ch.no1hardy.service.front.verification.VerifyEmailReq;
import ch.no1hardy.service.model.user.User;
import ch.no1hardy.service.model.verification.VerificationCode;
import ch.no1hardy.service.model.verification.VerificationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationService {
    private final VerificationCodeRepository verificationCodeRepository;

    public Optional<VerificationCode> getVerificationCode(String code) {
        return verificationCodeRepository.findByCode(code);
    }

    public void deleteVerificationCode(String code) {
        getVerificationCode(code)
                .ifPresent(verificationCodeRepository::delete);
    }

    public boolean verifyEmailCode(User user, VerifyEmailReq req) {
        return verifyCode(user.getMostRecentEmailVerificationCode(), req);
    }

    public boolean verifyCode(Optional<VerificationCode> code, VerifyEmailReq req) {
        return code.map(c -> verifyCode(c, req))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new VerificationException("Invalid verification code", "Ungültiger Verifizierungscode"));
    }

    public boolean verifyCode(VerificationCode code, VerifyEmailReq req) {
        return code.getCode().equals(req.code());
    }

    public void clearExpiredCodes() {
        verificationCodeRepository.findAll().stream()
                .filter(VerificationCode::isExpired)
                .forEach(verificationCodeRepository::delete);
    }

    public void saveVerificationCode(VerificationCode code) {
        verificationCodeRepository.save(code);
    }
}
