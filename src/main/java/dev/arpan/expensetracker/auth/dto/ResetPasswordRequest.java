package dev.arpan.expensetracker.auth.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/19/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordRequest {
    private Long id;
    private String token;
    private String newPassword;
}
