package dev.expensewise.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author arpan
 * @since 2/2/26
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyVerifiedException extends RuntimeException {
    public UserAlreadyVerifiedException(String email) {
        super("User with email " + email + " is already verified.");
    }
}
