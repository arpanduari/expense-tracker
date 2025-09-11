package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.expense.Expense;
import dev.arpan.expensetracker.user.User;

import java.time.LocalDate;
import java.util.List;

/**
 * @author arpan
 * @since 9/10/25
 */
public interface ReportGenerator {
    byte[] generate(User user, LocalDate startDate, LocalDate endDate, List<Expense> expenses);

    String getExtension();

    String getMediaType();

    public ReportType getType();
}
