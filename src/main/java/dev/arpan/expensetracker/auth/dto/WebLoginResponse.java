package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 12/21/25
 */
@Schema(description = "Data transfer object for Web Login response")
public record WebLoginResponse(
        @Schema(description = "accessToken for authentication") String accessToken,
        @Schema(description = "User name") String username) {}
