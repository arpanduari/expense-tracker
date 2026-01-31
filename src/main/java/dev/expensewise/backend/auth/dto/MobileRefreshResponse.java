package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 12/21/25
 */
@Schema(description = "Data transfer object for Mobile Refresh response")
public record MobileRefreshResponse(
        @Schema(description = "Access accessToken") String accessToken,
        @Schema(description = "Refresh accessToken") String refreshToken) {}
