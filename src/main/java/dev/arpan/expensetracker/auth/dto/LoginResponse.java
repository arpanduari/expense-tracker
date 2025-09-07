package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/2/25
 */
@Schema(description = "Data transfer object for Login Response")
public record LoginResponse(
        @Schema(description = "JWT token")
        String token,
        @Schema(description = "Refresh JWT token")
        String refreshToken,
        @Schema(description = "User name")
        String username
) {
}
