package dev.expensewise.backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/3/25
 */

@Schema(description = "Data transfer object for User")
public record UserResponse(
        @Schema(description = "username", example = "johndoe")
        String username,
        @Schema(description = "email", example = "")
        String email,
        @Schema(description = "currency", example = "INR")
        String currency,
        String avatarUrl) {
}
