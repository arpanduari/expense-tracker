package dev.arpan.expensetracker.ledger.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/24/25
 */
public record LedgerUserEntryResponse(
        Long id,
        String name,
        BigDecimal totalAmount,
        LocalDateTime lastUpdated) {
}