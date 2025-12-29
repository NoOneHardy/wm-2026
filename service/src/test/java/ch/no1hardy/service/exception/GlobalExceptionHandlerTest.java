package ch.no1hardy.service.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler;

    @BeforeEach
    public void beforeEach() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("should return storage exception as api error response")
    public void storageException01() {
        StorageException exception = new StorageException("Storage exception", "display message");

        ResponseEntity<ApiError> response500 = handler.handleApiException(exception);
        ApiError body = response500.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("Storage exception", body.getMessage());
        assertEquals("display message", body.getDisplayMessage());
    }

    @Test
    @DisplayName("should return storage exception with custom status as api error response")
    public void storageException02() {
        StorageException exception = new StorageException("Storage exception", "display message", HttpStatus.BAD_REQUEST);

        ResponseEntity<ApiError> response500 = handler.handleApiException(exception);
        ApiError body = response500.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Storage exception", body.getMessage());
        assertEquals("display message", body.getDisplayMessage());
    }
}
