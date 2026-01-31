package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 12/21/25
 */
@Schema(description = "Data transfer object for Web Refresh response")
public record WebRefreshResponse(
        @Schema(description = "accessToken for authentication")
        String accessToken,

        @Schema(description = "refreshToken to get accessToken in future")
        String refreshToken) {}
