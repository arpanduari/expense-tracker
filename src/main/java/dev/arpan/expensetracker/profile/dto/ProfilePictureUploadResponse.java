package dev.arpan.expensetracker.profile.dto;

/**
 * @author arpan
 * @since 8/30/25
 */
public record ProfilePictureUploadResponse(
        String profilePictureUrl,
        String message
) {
}
