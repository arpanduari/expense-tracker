package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/21/25
 */
@Schema(description = "Data transfer object for Reset Password Response")
public record ResetPasswordResponse(
        @Schema(description = "Message", example = "Password reset successfully")
        String message) {}
