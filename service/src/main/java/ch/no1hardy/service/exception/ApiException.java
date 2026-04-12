package ch.no1hardy.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public abstract class ApiException extends RuntimeException {
    protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private String message;
    private String displayMessage;

    public ApiException(String message, String displayMessage) {
        this.message = message;
        this.displayMessage = displayMessage;
    }
}
