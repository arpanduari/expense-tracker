package dev.arpan.expensetracker.ledger.dto;

import dev.arpan.expensetracker.ledger.EntryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/23/25
 */
public record LedgerEntryResponse(
        Long id,
        BigDecimal amount,
        EntryType type,
        String description,
        LocalDateTime createdDate
) {
}