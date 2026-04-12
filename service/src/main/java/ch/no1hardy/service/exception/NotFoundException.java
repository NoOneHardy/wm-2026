package ch.no1hardy.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseBody
@ResponseStatus(HttpStatus.NOT_FOUND)
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends ApiException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public NotFoundException(String message, String displayMessage) {
        super(message, displayMessage);
    }
}
