package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.FileReportResponse;
import dev.arpan.expensetracker.entity.Expense;
import dev.arpan.expensetracker.entity.User;

import java.time.LocalDate;
import java.util.List;

/**
 * @author arpan
 * @since 9/4/25
 */
public interface FileReportService {
    byte[] generateExcelReport(User user, LocalDate month, List<Expense> expenses);

    byte[] generatePdfReport(LocalDate startDate, LocalDate endDate);

    FileReportResponse generateMonthlyReport(Long userId, LocalDate month);

}
