package dev.arpan.expensetracker.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @author arpan
 * @since 10/3/25
 */
@Schema(description = "Data Transfer Object for Ledger Share Request")
public record LedgerShareRequest(
        @NotNull
        @Positive
        @Schema(description = "Ledger user id", example = "1")
        Long ledgerUserId
) {
}
