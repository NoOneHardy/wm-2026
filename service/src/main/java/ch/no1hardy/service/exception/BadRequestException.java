package ch.no1hardy.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends ApiException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message, String displayMessage) {
        super(message, displayMessage);
    }
}
