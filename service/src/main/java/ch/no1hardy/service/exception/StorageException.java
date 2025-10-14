package ch.no1hardy.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageException extends ApiException {
    private HttpStatus status;

    public StorageException(String message, String displayName) {
        this(message, displayName, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public StorageException(String message, String displayName, HttpStatus status) {
        super(message, displayName);
        this.status = status;
    }
}
