package dev.expensewise.backend.ledger.dto;

import dev.expensewise.backend.ledger.EntryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/22/25
 */
@Schema(description = "Data Transfer Object for creating new Ledger")
public record LedgerEntryRequest(
        @Schema(description = "Ledger user id", example = "1")
        @Positive
        Long ledgerUserId,
        @Schema(description = "Amount", example = "100.00")
        @NotNull
        BigDecimal amount,
        @Schema(description = "Entry type", example = "CREDIT")
        @NotNull
        EntryType type,
        @Schema(description = "Description", example = "Lend money for phone")
        @Size(max = 255)
        String description,
        @Schema(description = "Created date", example = "2025-01-01T00:00:00")
        LocalDateTime createdDate
) {
    public LocalDateTime createdDate() {
        return createdDate == null ? LocalDateTime.now() : createdDate;
    }
}
