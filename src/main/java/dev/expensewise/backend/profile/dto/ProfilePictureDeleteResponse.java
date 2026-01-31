package dev.expensewise.backend.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/30/25
 */
@Schema(description = "Data transfer object for Profile Picture Delete Response")
public record ProfilePictureDeleteResponse(
        @Schema(description = "Is profile picture deleted", example = "true")
        boolean deleted,
        @Schema(description = "Message", example = "Profile picture deleted successfully")
        String message) {
}
