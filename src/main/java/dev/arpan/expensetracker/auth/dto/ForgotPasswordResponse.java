package dev.arpan.expensetracker.auth.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/19/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPasswordResponse {
    private String message;
}
