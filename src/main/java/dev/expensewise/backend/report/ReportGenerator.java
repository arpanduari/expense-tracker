package dev.expensewise.backend.report;

import dev.expensewise.backend.expense.Expense;
import dev.expensewise.backend.user.User;

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

    ReportType getType();
}
