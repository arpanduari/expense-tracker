package dev.expensewise.backend.projection;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 9/11/25
 */
public interface IDailyExpense {
    Long getId();

    String getExpenseName();

    Double getAmount();

    String getCategory();

    String getDescription();

    LocalDate getCreatedDate();

    LocalTime getCreatedTime();
}
