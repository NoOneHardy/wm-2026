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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9-._]+@([A-Za-z0-9-]+\\.)+[A-Za-z0-9-]{2,}$");

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
        if (getUsername() != null) validateLength(5, 25, getUsername(), "username", "Benutzername");

        if (getFirstname() != null) validateLength(2, 25, getFirstname(), "firstname", "Vorname");

        if (getLastname() != null) validateLength(2, 25, getLastname(), "lastname", "Nachname");

        if (getEmail() != null) {
            validateLength(5, 50, getEmail(), "email", "Email");

            if (!EMAIL_PATTERN.matcher(getEmail()).matches()) {
                throw new UserValidationException("Invalid email format", "Ungültiges Email-Format");
            }
        }

        if (getPassword() != null) validateLength(8, 50, getPassword(), "password", "Passwort");
    }

    private void validateLength(int min, int max, String value, String name, String displayName) {
        if (value.length() < min) throw new UserValidationException(
                name + " must be at least " + min + " characters long",
                displayName + " muss mindestens " + min + " Zeichen lang sein."
        );

        if (value.length() > max) throw new UserValidationException(
                name + " can only contain " + max + " characters",
                displayName + " darf maximal " + max + " Zeichen enthalten."
        );
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
