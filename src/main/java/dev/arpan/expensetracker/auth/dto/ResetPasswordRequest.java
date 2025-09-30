package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/19/25
 */
@Schema(description = "Data transfer object for Reset Password Request")
public record ResetPasswordRequest(
        Long id,
        String token,
        String newPassword) {
}
