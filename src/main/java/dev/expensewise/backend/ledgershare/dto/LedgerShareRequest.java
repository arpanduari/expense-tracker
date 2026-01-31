package dev.expensewise.backend.ledgershare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * @author arpan
 * @since 10/3/25
 */
@Schema(description = "Data Transfer Object for Ledger Share Request")
public record LedgerShareRequest(
        @NotNull @Positive @Schema(description = "Ledger user id", example = "1")
        Long ledgerUserId,

        @Nullable @Schema(description = "Expiry duration of the share link", example = "P1D")
        Duration expiryDuration) {}
