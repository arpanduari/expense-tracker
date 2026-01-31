package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/5/25
 */
@Schema(description = "Data transfer object for Register Response")
public record RegisterResponse(
        @Schema(description = "Message", example = "Registration successful")
        String message,
        @Schema(description = "Verification URL")
        String verificationToken) {
}
