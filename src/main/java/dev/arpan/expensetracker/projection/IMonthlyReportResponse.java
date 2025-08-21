package dev.arpan.expensetracker.projection;

/**
 * @author arpan
 * @since 8/20/25
 */
public interface IMonthlyReportResponse {
    String getMonth();

    double getBudget();

    double getTotalExpenses();

    double getNetSavings();
}
