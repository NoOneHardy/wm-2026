package ch.no1hardy.service.user.application.dto;

import ch.no1hardy.service.user.domain.model.Email;
import ch.no1hardy.service.user.domain.model.UserApplicationStatus;
import ch.no1hardy.service.user.domain.model.UserRole;

import java.util.Optional;

public record UserProfileDto(
        String username,
        Email email,
        String firstname,
        String lastname,
        Optional<String> avatarUrl,
        UserRole role,
        UserApplicationStatus userApplicationStatus
) {
}
