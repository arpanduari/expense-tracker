package dev.arpan.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author arpan
 * @since 8/19/25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PasswordResetValidationResponse {
    private boolean isValid;
    private String message;
}
