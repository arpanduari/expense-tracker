package dev.arpan.expensetracker.report.dto;

/**
 * @author arpan
 * @since 8/23/25
 */
public record CategoryWiseTopExpenseResponse(
        String category,
        double amount,
        double percentage,
        String icon) {
}
