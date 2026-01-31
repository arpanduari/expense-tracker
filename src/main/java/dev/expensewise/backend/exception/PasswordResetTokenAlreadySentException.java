package dev.expensewise.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arpan
 * @since 8/21/25
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class PasswordResetTokenAlreadySentException extends RuntimeException {
    public PasswordResetTokenAlreadySentException(String message) {
        super(message);
    }
}
