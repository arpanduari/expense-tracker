package dev.expensewise.backend.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/24/25
 */
@Schema(description = "Data Transfer Object for Ledger User Entry")
public record LedgerUserEntryResponse(
        @Schema(description = "Ledger user id", example = "1")
        Long id,

        @Schema(description = "Ledger user name", example = "John Doe")
        String name,

        @Schema(description = "Profile image link if exists", example = "")
        String avatarUrl,

        @Schema(description = "Ledger user email", example = "johondoe@example.com")
        String email,

        @Schema(description = "Total amount (Credit - Debit)", example = "1000.00")
        BigDecimal totalAmount,

        @Schema(description = "Last updated date", example = "2025-01-01T00:00:00")
        LocalDateTime lastUpdated) {
}