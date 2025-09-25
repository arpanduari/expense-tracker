package dev.arpan.expensetracker.ledger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author arpan
 * @since 9/22/25
 */
public record LedgerUserRequest(
        @NotBlank
        @Size(min = 3, max = 50)
        String name,
        @NotBlank
        @Email
        String email) {
}
