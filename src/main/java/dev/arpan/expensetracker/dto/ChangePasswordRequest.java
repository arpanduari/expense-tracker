package dev.arpan.expensetracker.dto;


import lombok.*;

/**
 * @author arpan
 * @since 8/22/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
