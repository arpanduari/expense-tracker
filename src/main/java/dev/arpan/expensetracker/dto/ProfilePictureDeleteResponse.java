package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/30/25
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfilePictureDeleteResponse {
    private boolean deleted;
    private String message;
}
