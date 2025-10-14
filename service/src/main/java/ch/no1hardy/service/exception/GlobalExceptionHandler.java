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
                .body(new ApiError(ex.getMessage(), ex.getDisplayMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getDisplayMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiError> handleSignatureException() {
        return ResponseEntity.status(401)
                .body(new ApiError("Invalid token", "Ungültige Authentifikation", 401, LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException() {
        return ResponseEntity.status(401)
                .body(new ApiError("Invalid credentials", "Ungültige Anmeldedaten", 401, LocalDateTime.now()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException() {
        return ResponseEntity.status(403)
                .body(new ApiError("Not authorized to access this method", "Fehlende Berechtigungen", 403, LocalDateTime.now()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException() {
        return ResponseEntity.status(403)
                .body(new ApiError("This account has been disabled", "Dieser Account ist deaktiviert", 403, LocalDateTime.now()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredEntity(ExpiredJwtException ex) {
        return ResponseEntity.status(403).body(new ApiError(ex.getMessage(), "Die Session ist abgelaufen",403, LocalDateTime.now()));
    }

    @ExceptionHandler(BetPlaceException.class)
    public ResponseEntity<ApiError> handleBetPlaceException(BetPlaceException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getDisplayMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(KnockoutTieException.class)
    public ResponseEntity<ApiError> handleKnockoutTieException(KnockoutTieException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getDisplayMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ApiError> handleStorageException(StorageException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(new ApiError(ex.getMessage(), ex.getDisplayMessage(), ex.getStatus().value(), LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        return ResponseEntity.status(500)
                .body(new ApiError(ex.getMessage(), "Ein unerwarteter Fehler ist aufgetreten", 500, LocalDateTime.now()));
    }
}
