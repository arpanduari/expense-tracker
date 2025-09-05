package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.projection.ICategoryExpenseResponse;
import dev.arpan.expensetracker.projection.IInsightResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import dev.arpan.expensetracker.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

/**
 * @author arpan
 * @since 8/20/25
 */
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;


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


    public CategoryWiseMonthlyExpenseResponse getCategoryWiseMonthlyExpense(Long userId, LocalDate month) {
        if (month == null) {
            month = LocalDate.now();
        }
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        String monthYear = getMonthYear(startDate);
        List<ICategoryExpenseResponse> response = reportRepository.findCategoryExpenseByUserId(userId, startDate, endDate);
        List<CategoryExpenseResponse> result = response.stream()
                .map(ReportMapper::toCategoryExpenseResponse)
                .toList();
        return CategoryWiseMonthlyExpenseResponse.builder()
                .month(monthYear)
                .categoryWiseExpenses(result)
                .build();
    }


    public YearlyReportResponse getYearlyReport(Long userId, Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        Map<String, MonthlyYearResponse> monthlyReports = new LinkedHashMap<>();
        for (int month = 1; month <= 12; ++month) {
            LocalDate date = LocalDate.of(year, month, 1);
            String monthName = getMonthName(date);
            try {
                MonthlyReportResponse response = getMonthlyReport(userId, date);
                monthlyReports.put(monthName, ReportMapper.toMonthlyYearResponse(response));
            } catch (ResourceNotFoundException e) {
                monthlyReports.put(
                        monthName,
                        MonthlyYearResponse.builder()
                                .budget(0.0d)
                                .totalExpenses(0.0d)
                                .netSavings(0.0d)
                                .build()
                );
            }
        }
        return YearlyReportResponse.builder()
                .monthlyReports(monthlyReports)
                .year(year)
                .build();
    }


    public TopExpenseResponse getTopExpense(Long userId, LocalDate month, int limit) {
        if (month == null) {
            month = LocalDate.now();
        }

        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        List<CategoryWiseTopExpense> categoryWiseTopExpenses = reportRepository
                .findCategoryWiseTopExpense(userId, limit, startDate, endDate)
                .stream()
                .map(ReportMapper::toCategoryWiseTopExpense)
                .toList();
        return TopExpenseResponse
                .builder()
                .month(getMonthName(startDate))
                .year(getYear(startDate))
                .topExpenses(categoryWiseTopExpenses)
                .build();
    }


    public InsightResponse getInsight(Long userId, LocalDate month) {
        if (month == null) {
            month = LocalDate.now();
        }
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        Optional<IInsightResponse> insightResponse = reportRepository.findInsight(userId, startDate, endDate);
        InsightResponse response = insightResponse.map(ReportMapper::toInsightResponse)
                .orElse(
                        InsightResponse.builder()
                                .build()
                );
        response.setMonth(getMonthName(startDate));
        response.setYear(getYear(startDate));
        return response;
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
