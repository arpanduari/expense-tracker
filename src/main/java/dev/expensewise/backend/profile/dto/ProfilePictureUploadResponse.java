package dev.expensewise.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/30/25
 */
@Schema(description = "Data transfer object for Profile Picture Upload Response")
public record ProfilePictureUploadResponse(
        @Schema(description = "Profile picture url", example = "https://example.com/profile.jpg")
        String profilePictureUrl,
        @Schema(description = "Message", example = "Profile picture uploaded successfully")
        String message
) {
}
