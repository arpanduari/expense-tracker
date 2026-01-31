package dev.expensewise.backend.projection;

/**
 * @author arpan
 * @since 8/23/25
 */
public interface ICategoryWiseTopExpense {
    String getCategory();

    double getAmount();

    double getPercentage();

    String getIcon();
}
