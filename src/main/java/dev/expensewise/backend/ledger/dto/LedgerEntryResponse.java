package dev.expensewise.backend.ledger.dto;

import dev.expensewise.backend.ledger.EntryType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/23/25
 */
@Schema(description = "Data Transfer Object for Ledger Entry Response")
public record LedgerEntryResponse(
        @Schema(description = "Ledger entry id", example = "1")
        Long id,
        @Schema(description = "Amount", example = "100.00")
        BigDecimal amount,
        @Schema(description = "Entry type", example = "CREDIT")
        EntryType type,
        @Schema(description = "Description", example = "Lend money for phone")
        String description,
        @Schema(description = "Created date", example = "2025-01-01T00:00:00")
        LocalDateTime createdDate
) {
}