package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.projection.ICategoryExpenseResponse;
import dev.arpan.expensetracker.projection.ICategoryWiseTopExpense;
import dev.arpan.expensetracker.projection.IInsightResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import dev.arpan.expensetracker.report.dto.*;

/**
 * @author arpan
 * @since 8/20/25
 */
public final class ReportMapper {
    private ReportMapper() {
    }

    public static MonthlyReportResponse toMonthlyReportResponse(IMonthlyReportResponse monthlyReportResponse) {
        return new MonthlyReportResponse(monthlyReportResponse.getMonth(), monthlyReportResponse.getBudget(),
                monthlyReportResponse.getTotalExpenses(), monthlyReportResponse.getNetSavings());
    }

    public static CategoryExpenseResponse toCategoryExpenseResponse(ICategoryExpenseResponse categoryExpenseResponse) {
        return new CategoryExpenseResponse(categoryExpenseResponse.getCategory(), categoryExpenseResponse.getAmount(),
                categoryExpenseResponse.getPercentage(), categoryExpenseResponse.getIcon());
    }

    public static MonthlyYearResponse toMonthlyYearResponse(MonthlyReportResponse monthlyReportResponse) {
        return new MonthlyYearResponse(monthlyReportResponse.getBudget(), monthlyReportResponse.getTotalExpenses(),
                monthlyReportResponse.getNetSavings());
    }

    public static CategoryWiseTopExpenseResponse toCategoryWiseTopExpense(ICategoryWiseTopExpense categoryWiseTopExpense) {
        return new CategoryWiseTopExpenseResponse(categoryWiseTopExpense.getCategory(), categoryWiseTopExpense.getAmount(),
                categoryWiseTopExpense.getPercentage(), categoryWiseTopExpense.getIcon());
    }

    public static InsightResponse toInsightResponse(IInsightResponse insightResponse) {
        return InsightResponse.builder()
                .mostExpensiveDay(insightResponse.getMostExpensiveDay())
                .amountOnMostExpensiveDay(insightResponse.getAmountOnMostExpensiveDay())
                .averageDailySpending(insightResponse.getAverageDailySpending())
                .expensiveCategory(insightResponse.getExpensiveCategory())
                .expensiveCategorySpending(insightResponse.getExpensiveCategorySpending())
                .totalSpending(insightResponse.getTotalSpending())
                .build();
    }

}
