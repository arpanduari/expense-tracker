package dev.arpan.expensetracker.projection;

/**
 * @author arpan
 * @since 8/22/25
 */
public interface ICategoryExpenseResponse {
    String getCategory();

    double getAmount();

    double getPercentage();

    String getIcon();
}
