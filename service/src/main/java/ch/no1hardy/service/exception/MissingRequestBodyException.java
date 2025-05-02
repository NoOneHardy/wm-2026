package ch.no1hardy.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseBody
@ResponseStatus(HttpStatus.BAD_REQUEST)
@EqualsAndHashCode(callSuper = false)
public class MissingRequestBodyException extends RuntimeException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public MissingRequestBodyException() {
        super("Missing request body");
    }
}
