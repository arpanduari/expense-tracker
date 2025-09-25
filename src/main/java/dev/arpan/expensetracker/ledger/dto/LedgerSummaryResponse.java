package dev.arpan.expensetracker.ledger.dto;

import java.math.BigDecimal;

/**
 * @author arpan
 * @since 9/22/25
 */
public record LedgerSummaryResponse(
        Long ledgerUserId,
        String ledgerUsername,
        BigDecimal totalCredit,
        BigDecimal totalDebit,
        BigDecimal totalBalance
) {
}
