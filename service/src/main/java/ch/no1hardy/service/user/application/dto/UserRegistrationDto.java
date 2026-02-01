package ch.no1hardy.service.user.application.dto;

public record UserRegistrationDto(
        String username,
        String password,
        String email,
        String firstname,
        String lastname
) {
}
