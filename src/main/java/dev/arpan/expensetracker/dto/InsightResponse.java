package dev.arpan.expensetracker.dto;

/**
 * @author arpan
 * @since 8/20/25
 */
public class InsightResponse {
    private String month;
    private String mostExpensiveDay;
    private double amountOnMostExpensiveDay;
    private double averageDailySpending;
    private Integer daysWithoutSpending;
}
