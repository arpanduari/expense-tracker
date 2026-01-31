package dev.expensewise.backend.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/22/25
 */
@Schema(description = "Data transfer object for Change Password Request")
public record ChangePasswordRequest(
        @Schema(description = "Old password of the user", example = "veryHardPassword@123")
        String oldPassword,
        @Schema(description = "New password", example = "veryVeryHardPassword@123")
        String newPassword) {
}
