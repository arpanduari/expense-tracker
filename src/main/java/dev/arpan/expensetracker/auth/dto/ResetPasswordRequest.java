package dev.arpan.expensetracker.auth.dto;

/**
 * @author arpan
 * @since 8/19/25
 */
public record ResetPasswordRequest(
        Long id,
        String token,
        String newPassword) {
}
