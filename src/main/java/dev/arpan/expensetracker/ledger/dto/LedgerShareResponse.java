package dev.arpan.expensetracker.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 10/3/25
 */
@Schema(description = "Data Transfer Object for Ledger Share Response")
public record LedgerShareResponse(
        String publicLink
) {
}
