package ch.no1hardy.service.front.user;

import ch.no1hardy.service.exception.user.UserValidationException;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class UserReq {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String avatar;
    private PasswordChange passwordChange;

    /**
     * Complete validation of the user request.
     *
     * @throws UserValidationException if any validation fails
     */
    public void validateAll() throws UserValidationException {
        validateRequired();
        validate();
    }

    /**
     * Validates the user request.
     * This method should be implemented to check the validity of the user data.
     *
     * @throws UserValidationException if the validation fails
     */
    public void validate() throws UserValidationException {
        if (getUsername() != null) {
            if (getUsername().length() < 5) {
                throw new UserValidationException("Username must be at least 5 characters long", "Der Benutzername muss mindestens 5 Zeichen lang sein.");
            }

            if (getUsername().length() > 25) {
                throw new UserValidationException("Username can only contain 25 characters", "Der Benutzername darf maximal 25 Zeichen enthalten.");
            }
        }

        if (getFirstname() != null) {
            if (getFirstname().length() < 2) {
                throw new UserValidationException("Firstname must be at least 2 characters long", "Der Vorname muss mindestens 2 Zeichen lang sein.");
            }

            if (getFirstname().length() > 25) {
                throw new UserValidationException("Firstname can only contain 25 characters", "Der Vorname darf maximal 25 Zeichen enthalten.");
            }
        }

        if (getLastname() != null) {
            if (getLastname().length() < 2) {
                throw new UserValidationException("Lastname must be at least 2 characters long", "Der Nachname muss mindestens 2 Zeichen lang sein.");
            }

            if (getLastname().length() > 25) {
                throw new UserValidationException("Lastname can only contain 25 characters", "Der Nachname darf maximal 25 Zeichen enthalten.");
            }
        }

        if (getEmail() != null && !getEmail().isEmpty()) {
            if (!Pattern.compile("^[A-z0-9-.]+@([A-z0-9-]+\\.)+[A-z-]{2,4}$").matcher(getEmail()).matches()) {
                throw new UserValidationException("Invalid email format", "Ungültiges Email-Format");
            }
        }

        if (getPassword() != null && !getPassword().isEmpty()) {
            if (getPassword().length() < 8) {
                throw new UserValidationException("Password must be at least 8 characters long", "Das Passwort muss mindestens 8 Zeichen lang sein");
            }

        }
    }

    /**
     * Validates the required fields of the user request.
     * This method should be implemented to check that all required fields are present.
     *
     * @throws UserValidationException if any required field is missing
     */
    public void validateRequired() throws UserValidationException {
        if (getUsername() == null || getUsername().isEmpty())
            throw new UserValidationException("username");

        if (getEmail() == null || getEmail().isEmpty())
            throw new UserValidationException("email");

        if (getPassword() == null || getPassword().isEmpty())
            throw new UserValidationException("password");

        if (getFirstname() == null || getFirstname().isEmpty())
            throw new UserValidationException("firstname");

        if (getLastname() == null || getLastname().isEmpty())
            throw new UserValidationException("lastname");
    }

    public record PasswordChange(String password, String currentPassword) {
    }
}
