package dev.arpan.expensetracker.projection;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/24/25
 */
public interface IInsightResponse {
    LocalDate getMostExpensiveDay();

    Double getAmountOnMostExpensiveDay();

    Double getAverageDailySpending();

    String getExpensiveCategory();

    Double getExpensiveCategorySpending();

    Double getTotalSpending();
}
