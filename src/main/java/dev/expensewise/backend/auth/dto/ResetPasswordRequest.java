package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/19/25
 */
@Schema(description = "Data transfer object for Reset Password Request")
public record ResetPasswordRequest(
        @Schema(description = "id") Long id,
        @Schema(description = "Reset token") String token,
        @Schema(description = "New password") String newPassword) {}
