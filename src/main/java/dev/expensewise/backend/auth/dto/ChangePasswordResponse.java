package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/22/25
 */
@Schema(description = "Data transfer object for Change Password Response")
public record ChangePasswordResponse(
        @Schema(description = "True if password changed successfully", example = "true")
        boolean isPasswordChanged,
        @Schema(description = "Message", example = "Password changed successfully")
        String message
) {
}
