package dev.expensewise.backend.ledgershare.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * @author arpan
 * @since 12/22/25
 */
public record LedgerShareDetailsResponse(
        UUID linkId,
        Instant expiryAt
){}
