package dev.expensewise.backend.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 9/22/25
 */
@Schema(description = "Data Transfer Object for Ledger User Response")
public record LedgerUserResponse(
        @Schema(description = "Ledger user id", example = "1")
        Long id,
        @Schema(description = "Ledger user name", example = "John Doe")
        String name,
        @Schema(description = "Ledger user email", example = "johndoe@example.com")
        String email) {
}
