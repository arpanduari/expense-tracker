package dev.arpan.expensetracker.exception;

/**
 * @author arpan
 * @since 12/21/25
 */
public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
