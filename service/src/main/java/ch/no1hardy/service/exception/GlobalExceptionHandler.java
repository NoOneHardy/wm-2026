package ch.no1hardy.service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(NotFoundException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }
}
