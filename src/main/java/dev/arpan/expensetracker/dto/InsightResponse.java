package dev.arpan.expensetracker.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsightResponse {
    private String month;
    private Integer year;
    private LocalDate mostExpensiveDay;
    private double amountOnMostExpensiveDay;
    private double averageDailySpending;
    private String expensiveCategory;
    private double expensiveCategorySpending;
    private double totalSpending;
}