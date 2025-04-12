package ch.no1hardy.service.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiError> handleSignatureException() {
        return ResponseEntity.status(401)
                .body(new ApiError("Invalid token", 401, LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException() {
        return ResponseEntity.status(401)
                .body(new ApiError("Invalid credentials", 401, LocalDateTime.now()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException() {
        return ResponseEntity.status(403)
                .body(new ApiError("Not authorized to access this method", 403, LocalDateTime.now()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException() {
        return ResponseEntity.status(403)
                .body(new ApiError("This account has been disabled", 403, LocalDateTime.now()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredEntity(ExpiredJwtException ex) {
        return ResponseEntity.status(403).body(new ApiError(ex.getMessage(), 403, LocalDateTime.now()));
    }
}
