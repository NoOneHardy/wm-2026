package ch.no1hardy.service.model.verification;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends ListCrudRepository<VerificationCode, String> {
    Optional<VerificationCode> findByCode(String code);
}
