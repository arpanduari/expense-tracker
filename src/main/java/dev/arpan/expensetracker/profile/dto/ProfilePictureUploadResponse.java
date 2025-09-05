package dev.arpan.expensetracker.profile.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author arpan
 * @since 8/30/25
 */
@Getter
@Setter
@Builder
public class ProfilePictureUploadResponse {
    private String profilePictureUrl;
    private String message;
}
