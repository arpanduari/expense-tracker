package dev.expensewise.backend.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * @author arpan
 * @since 9/22/25
 */
@Schema(description = "Data transfer object for Ledger Summary")
public record LedgerSummaryResponse(
        @Schema(description = "Ledger user id", example = "1")
        Long ledgerUserId,
        @Schema(description = "Ledger user name", example = "John Doe")
        String ledgerUsername,
        @Schema(description = "Total credit amount", example = "1000.00")
        BigDecimal totalCredit,
        @Schema(description = "Total debit amount", example = "100.00")
        BigDecimal totalDebit,
        @Schema(description = "Total balance", example = "990.00")
        BigDecimal totalBalance
) {
}
