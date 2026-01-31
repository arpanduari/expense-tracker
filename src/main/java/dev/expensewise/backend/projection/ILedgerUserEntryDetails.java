package dev.expensewise.backend.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/23/25
 */
public interface ILedgerUserEntryDetails {
    Long getId();

    String getName();

    String getAvatarUrl();

    String getEmail();

    BigDecimal getTotalAmount();

    LocalDateTime getLastUpdated();
}
