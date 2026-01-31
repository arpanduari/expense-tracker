package dev.expensewise.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arpan
 * @since 10/9/25
 */
@ResponseStatus(HttpStatus.LOCKED)
public class OTPNotVerifiedException extends RuntimeException {
    public OTPNotVerifiedException(String message) {
        super(message);
    }
}
