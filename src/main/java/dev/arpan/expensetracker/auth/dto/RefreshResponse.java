package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/4/25
 */
@Schema(description = "Data transfer object for Refresh Response")
public record RefreshResponse(
        @Schema(description = "Access token")
        String accessToken,
        @Schema(description = "Refresh token")
        String refreshToken) {
}
