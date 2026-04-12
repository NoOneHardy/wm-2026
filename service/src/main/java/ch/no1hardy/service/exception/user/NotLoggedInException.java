package ch.no1hardy.service.exception.user;

import ch.no1hardy.service.exception.ApiException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class NotLoggedInException extends ApiException {
  private final HttpStatus status = HttpStatus.UNAUTHORIZED;

    public NotLoggedInException() {
        super("Not logged in", "Du bist nicht eingeloggt");
    }
}
