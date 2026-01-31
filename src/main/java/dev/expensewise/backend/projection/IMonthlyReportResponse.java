package dev.expensewise.backend.projection;

/**
 * @author arpan
 * @since 8/20/25
 */
public interface IMonthlyReportResponse {
    String getMonth();

    Double getBudget();

    Double getTotalExpenses();

    Double getNetSavings();
}
