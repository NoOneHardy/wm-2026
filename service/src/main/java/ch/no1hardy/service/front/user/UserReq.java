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
