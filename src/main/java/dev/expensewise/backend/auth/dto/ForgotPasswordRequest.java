package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/19/25
 */
@Schema(description = "Data transfer object for Forgot Password Request")
public record ForgotPasswordRequest(
        @Schema(description = "Email of the user the password should be reset", example = "johndoe@example.com")
        String email) {
}
