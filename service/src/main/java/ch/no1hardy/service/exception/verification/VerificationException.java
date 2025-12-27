package ch.no1hardy.service.exception.verification;

import ch.no1hardy.service.exception.ApiException;
import org.springframework.http.HttpStatus;

public class VerificationException extends ApiException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public VerificationException(String message, String displayMessage) {
        super(message, displayMessage);
    }
}
