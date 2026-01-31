package dev.expensewise.backend.report;

import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.projection.ICategoryExpenseResponse;
import dev.expensewise.backend.projection.IDailyExpense;
import dev.expensewise.backend.projection.IInsightResponse;
import dev.expensewise.backend.projection.IMonthlyReportResponse;
import dev.expensewise.backend.report.dto.*;
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
    private final ReportMapper reportMapper;

    public MonthlyReportResponse getMonthlyReport(Long userId, LocalDate month) {
        month = getBaseMonth(month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        String monthYear = getMonthYear(startDate);

        IMonthlyReportResponse monthlyReportResponse = reportRepository.findMonthlyReport(userId, startDate, endDate)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly Report", "date", monthYear));

        MonthlyReportResponse mappedResult = reportMapper.toMonthlyReportResponse(monthlyReportResponse);
        mappedResult.setMonth(monthYear);
        return mappedResult;
    }


    public CategoryWiseMonthlyExpenseResponse getCategoryWiseMonthlyExpense(Long userId, LocalDate month) {
        month = getBaseMonth(month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        String monthYear = getMonthYear(startDate);
        List<ICategoryExpenseResponse> response = reportRepository.findCategoryExpenseByUserId(userId, startDate, endDate);
        List<CategoryExpenseResponse> result = response.stream()
                .map(reportMapper::toCategoryExpenseResponse)
                .toList();
        return new CategoryWiseMonthlyExpenseResponse(monthYear, result);
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
                monthlyReports.put(monthName, reportMapper.toMonthlyYearResponse(response));
            } catch (ResourceNotFoundException e) {
                monthlyReports.put(
                        monthName,
                        new MonthlyYearResponse(0.0d, 0.0d, 0.0d)
                );
            }
        }
        return new YearlyReportResponse(year, monthlyReports);
    }


    public TopExpenseResponse getTopExpense(Long userId, LocalDate month, int limit) {
        month = getBaseMonth(month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        List<CategoryWiseTopExpenseResponse> categoryWiseTopExpenses = reportRepository
                .findCategoryWiseTopExpense(userId, limit, startDate, endDate)
                .stream()
                .map(reportMapper::toCategoryWiseTopExpense)
                .toList();
        return TopExpenseResponse
                .builder()
                .month(getMonthName(startDate))
                .year(getYear(startDate))
                .topExpenses(categoryWiseTopExpenses)
                .build();
    }


    public InsightResponse getInsight(Long userId, LocalDate month) {
        month = getBaseMonth(month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        Optional<IInsightResponse> insightResponse = reportRepository.findInsight(userId, startDate, endDate);

        InsightResponse response = insightResponse.map(reportMapper::toInsightResponse)
                .orElse(InsightResponse.builder().build());

        response.setMonth(getMonthName(startDate));
        response.setYear(getYear(startDate));

        return response;
    }

    public Map<LocalDate, List<DailyExpenseResponse>> getExpensesGroupByDate(Long userId, LocalDate month) {
        month = getBaseMonth(month);
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        List<IDailyExpense> dailyExpenses = reportRepository.findExpenseInRange(userId, startDate, endDate);
        return reportMapper.getExpensesGroupByDate(dailyExpenses);
    }


    public LocalDate getBaseMonth(LocalDate month) {
        return Optional.ofNullable(month).orElseGet(LocalDate::now);
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
