package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/20/25
 */
public interface ReportService {
    MonthlyReportResponse getMonthlyReport(Long userId, LocalDate month);
    CategoryWiseMonthlyExpenseResponse getCategoryWiseMonthlyExpense(Long userId, LocalDate month);
    YearlyReportResponse getYearlyReport(LocalDate year);
    TopExpenseResponse getTopExpense(LocalDate month, int limit);
    InsightResponse getInsight(LocalDate month);
}

