package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/2/25
 */
@Schema(description = "Data transfer object for Login Response")
public record LoginResponse(
        @Schema(description = "JWT accessToken")
        String accessToken,
        @Schema(description = "Refresh JWT accessToken")
        String refreshToken,
        @Schema(description = "User name")
        String username
) {
}
