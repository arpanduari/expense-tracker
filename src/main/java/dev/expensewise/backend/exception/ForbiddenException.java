package dev.expensewise.backend.exception;

/**
 * @author arpan
 * @since 9/24/25
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
