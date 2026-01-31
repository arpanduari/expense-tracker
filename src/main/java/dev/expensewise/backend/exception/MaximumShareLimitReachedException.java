package dev.expensewise.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arpan
 * @since 12/22/25
 */
@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class MaximumShareLimitReachedException extends RuntimeException {
    public MaximumShareLimitReachedException(String message) {
        super(message);
    }
}
