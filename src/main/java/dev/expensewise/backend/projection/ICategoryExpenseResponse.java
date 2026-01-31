package dev.expensewise.backend.projection;

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
