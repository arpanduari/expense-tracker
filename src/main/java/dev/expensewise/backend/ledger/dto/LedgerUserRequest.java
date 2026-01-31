package dev.expensewise.backend.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author arpan
 * @since 9/22/25
 */
@Schema(description = "Data Transfer Object for creating new Ledger User")
public record LedgerUserRequest(
        @Schema(description = "Ledger user name", example = "John Doe")
        @NotBlank
        @Size(min = 3, max = 50)
        String name,
        @Schema(description = "Ledger user email", example = "johndoe@example.com")
        @NotBlank
        @Email
        String email) {
}
