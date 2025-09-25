package dev.arpan.expensetracker.ledger.dto;

import dev.arpan.expensetracker.ledger.EntryType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/22/25
 */
public record LedgerEntryRequest(
        @Positive
        Long ledgerUserId,
        BigDecimal amount,
        @NotNull
        EntryType type,
        @Size(max = 255)
        String description,
        LocalDateTime createdDate
) {
    public LocalDateTime createdDate() {
        return createdDate == null ? LocalDateTime.now() : createdDate;
    }
}
