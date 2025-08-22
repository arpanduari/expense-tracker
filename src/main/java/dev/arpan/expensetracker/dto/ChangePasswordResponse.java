package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/22/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordResponse {
    private boolean isPasswordChanged;
    private String message;
}
