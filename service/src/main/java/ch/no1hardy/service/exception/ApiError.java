package ch.no1hardy.service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private final String message;
    private final Integer status;
    private final LocalDateTime timestamp;
}
