package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/19/25
 */
@Schema(description = "Data transfer object for Forgot Password Response")
public record ForgotPasswordResponse(
        @Schema(description = "Forgot Password Message", example = "Password reset email sent successfully")
        String message) {
}
