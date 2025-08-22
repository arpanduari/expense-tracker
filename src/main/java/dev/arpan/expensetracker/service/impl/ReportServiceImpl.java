package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.mapper.ReportMapper;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import dev.arpan.expensetracker.repository.ReportRepository;
import dev.arpan.expensetracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * @author arpan
 * @since 8/20/25
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Override
    public MonthlyReportResponse getMonthlyReport(Long userId, LocalDate month) {
        if (month == null) {
            month = LocalDate.now();
        }

        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        String monthYear = getMonthYear(startDate);

        IMonthlyReportResponse monthlyReportResponse = reportRepository.findMonthlyReport(userId, startDate, endDate)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly Report", "date", monthYear));

        MonthlyReportResponse mappedResult = ReportMapper.toMonthlyReportResponse(monthlyReportResponse);
        mappedResult.setMonth(monthYear);
        return mappedResult;
    }

    @Override
    public CategoryWiseMonthlyExpenseResponse getCategoryWiseMonthlyExpense(LocalDate month) {
        if (month == null) {
            month = LocalDate.now();
        }
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        String monthYear = getMonthYear(startDate);

        return null;
    }

    @Override
    public YearlyReportResponse getYearlyReport(LocalDate year) {
        return null;
    }

    @Override
    public TopExpenseResponse getTopExpense(LocalDate month, int limit) {
        return null;
    }

    @Override
    public InsightResponse getInsight(LocalDate month) {
        return null;
    }

    public static String getMonthName(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public static Integer getYear(LocalDate date) {
        return date.getYear();
    }

    public static String getMonthYear(LocalDate date) {
        return getMonthName(date) + "-" + getYear(date);
    }
}
