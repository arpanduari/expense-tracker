package dev.expensewise.backend.ledgershare.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * @author arpan
 * @since 10/3/25
 */
@Schema(description = "Data Transfer Object for Ledger Share Response")
public record LedgerShareResponse(
        UUID id,
        String publicLink
) {
}
