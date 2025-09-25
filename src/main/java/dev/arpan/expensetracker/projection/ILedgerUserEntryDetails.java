package dev.arpan.expensetracker.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 9/23/25
 */
public interface ILedgerUserEntryDetails {
    Long getId();

    String getName();

    BigDecimal getTotalAmount();

    LocalDateTime getLastUpdated();
}
