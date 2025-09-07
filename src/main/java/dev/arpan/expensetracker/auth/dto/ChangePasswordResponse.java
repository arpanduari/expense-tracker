package dev.arpan.expensetracker.auth.dto;

/**
 * @author arpan
 * @since 8/22/25
 */
public record ChangePasswordResponse(
        boolean isPasswordChanged,
        String message
) {
}
