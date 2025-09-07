package dev.arpan.expensetracker.report.dto;

import java.util.Map;

/**
 * @author arpan
 * @since 8/20/25
 */
public record YearlyReportResponse(
        Integer year,
        Map<String, MonthlyYearResponse> monthlyReports) {
}
