package dev.arpan.expensetracker.dto;

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
public class ProfilePictureResponse {
    private String profilePictureUrl;
}
