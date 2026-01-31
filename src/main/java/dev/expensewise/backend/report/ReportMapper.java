package dev.expensewise.backend.report;

import dev.expensewise.backend.projection.*;
import dev.expensewise.backend.report.dto.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author arpan
 * @since 8/20/25
 */
@Component
public class ReportMapper {

    public MonthlyReportResponse toMonthlyReportResponse(IMonthlyReportResponse monthlyReportResponse) {
        return new MonthlyReportResponse(monthlyReportResponse.getMonth(), monthlyReportResponse.getBudget(),
                monthlyReportResponse.getTotalExpenses(), monthlyReportResponse.getNetSavings());
    }

    public CategoryExpenseResponse toCategoryExpenseResponse(ICategoryExpenseResponse categoryExpenseResponse) {
        return new CategoryExpenseResponse(categoryExpenseResponse.getCategory(), categoryExpenseResponse.getAmount(),
                categoryExpenseResponse.getPercentage(), categoryExpenseResponse.getIcon());
    }

    public MonthlyYearResponse toMonthlyYearResponse(MonthlyReportResponse monthlyReportResponse) {
        return new MonthlyYearResponse(monthlyReportResponse.getBudget(), monthlyReportResponse.getTotalExpenses(),
                monthlyReportResponse.getNetSavings());
    }

    public CategoryWiseTopExpenseResponse toCategoryWiseTopExpense(ICategoryWiseTopExpense categoryWiseTopExpense) {
        return new CategoryWiseTopExpenseResponse(categoryWiseTopExpense.getCategory(), categoryWiseTopExpense.getAmount(),
                categoryWiseTopExpense.getPercentage(), categoryWiseTopExpense.getIcon());
    }

    public InsightResponse toInsightResponse(IInsightResponse insightResponse) {
        return InsightResponse.builder()
                .mostExpensiveDay(insightResponse.getMostExpensiveDay())
                .amountOnMostExpensiveDay(insightResponse.getAmountOnMostExpensiveDay())
                .averageDailySpending(insightResponse.getAverageDailySpending())
                .expensiveCategory(insightResponse.getExpensiveCategory())
                .expensiveCategorySpending(insightResponse.getExpensiveCategorySpending())
                .totalSpending(insightResponse.getTotalSpending())
                .build();
    }

    public DailyExpenseResponse toDailyExpenseResponse(IDailyExpense dailyExpense) {
        return new DailyExpenseResponse(dailyExpense.getId(), dailyExpense.getExpenseName(), dailyExpense.getAmount(),
                dailyExpense.getCategory(), dailyExpense.getDescription(), dailyExpense.getCreatedDate(),
                dailyExpense.getCreatedTime()
        );
    }

    public Map<LocalDate, List<DailyExpenseResponse>> getExpensesGroupByDate(List<IDailyExpense> dailyExpenses) {
        return dailyExpenses.stream()
                .map(this::toDailyExpenseResponse)
                .collect(Collectors.groupingBy(DailyExpenseResponse::createdDate));
    }
}
