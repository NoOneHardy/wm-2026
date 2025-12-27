package ch.no1hardy.service.model.verification;

import ch.no1hardy.service.model.BaseEntity;
import ch.no1hardy.service.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class VerificationCode extends BaseEntity {
    private final String code = this.generateCode();
    private LocalDateTime expiresAt;
    private VerificationCodeType type;

    @ManyToOne
    private User user;

    public VerificationCode(VerificationCodeType type, User user) {
        this(10, type, user);
    }

    public VerificationCode(int validMinutes, VerificationCodeType type, User user) {
        this(LocalDateTime.now().plusMinutes(validMinutes), type, user);
    }

    public VerificationCode(LocalDateTime expiresAt, VerificationCodeType type, User user) {
        super();
        this.expiresAt = expiresAt;
        this.type = type;
        this.user = user;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !isExpired();
    }

    private String generateCode() {
        String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        return uuid.substring(uuid.length() - 6);
    }
}
