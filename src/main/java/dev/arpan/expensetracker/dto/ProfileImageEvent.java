package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/31/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileImageEvent {
    private Long userId;
    private String username;
    private String publicId;
    private String secureUrl;
}
