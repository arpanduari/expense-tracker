package dev.expensewise.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arpan
 * @since 8/22/25
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordNotMatchingException extends RuntimeException {
    public PasswordNotMatchingException(String message) {
        super(message);
    }
}
