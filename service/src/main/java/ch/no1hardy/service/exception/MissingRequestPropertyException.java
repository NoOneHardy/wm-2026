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
public class MissingRequestPropertyException extends ApiException {
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public MissingRequestPropertyException(String property, String type) {
        super("Request body is missing property '" + property + "' of type '" + type + "'", "Die Anfrage benötigt das Property '" + property + "'");
    }
}
