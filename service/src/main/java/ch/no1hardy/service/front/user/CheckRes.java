package ch.no1hardy.service.front.user;

public record CheckRes(
        Boolean isUsernameAvailable,
        Boolean isEmailAvailable
) {
}
