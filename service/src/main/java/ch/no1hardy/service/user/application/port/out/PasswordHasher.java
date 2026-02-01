package ch.no1hardy.service.user.application.port.out;

public interface PasswordHasher {
    String hash(String password);
    boolean matches(String rawPassword, String hashedPassword);
}
