package ch.no1hardy.service.user.domain.model;

import java.time.LocalDateTime;
import java.util.Optional;

public record Email(
        String address,
        Optional<LocalDateTime> verifiedAt
) {
    public Email(String address) {
        this(address, Optional.empty());
    }

    public boolean isVerified() {
        return verifiedAt.isPresent();
    }

    public Email verify() {
        if (isVerified()) return this;
        return new Email(address, Optional.of(LocalDateTime.now()));
    }
}
