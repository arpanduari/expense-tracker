package dev.arpan.expensetracker.report.dto;

/**
 * @author arpan
 * @since 8/23/25
 */
public record MonthlyYearResponse(
        double budget,
        double totalExpenses,
        double netSavings) {
}
