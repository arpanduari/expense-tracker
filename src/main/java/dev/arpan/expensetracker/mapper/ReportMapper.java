package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.projection.ICategoryExpenseResponse;
import dev.arpan.expensetracker.projection.ICategoryWiseTopExpense;
import dev.arpan.expensetracker.projection.IInsightResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;

import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
public final class ReportMapper {
    private ReportMapper() {
    }

    public static MonthlyReportResponse toMonthlyReportResponse(IMonthlyReportResponse monthlyReportResponse) {
        return MonthlyReportResponse.builder()
                .month(monthlyReportResponse.getMonth())
                .budget(Optional.ofNullable(monthlyReportResponse.getBudget()).orElse(0.0d))
                .netSavings(Optional.ofNullable(monthlyReportResponse.getNetSavings()).orElse(0.0d))
                .totalExpenses(Optional.ofNullable(monthlyReportResponse.getTotalExpenses()).orElse(0.0d))
                .build();
    }

    public static CategoryExpenseResponse toCategoryExpenseResponse(ICategoryExpenseResponse categoryExpenseResponse) {
        return CategoryExpenseResponse.builder()
                .category(categoryExpenseResponse.getCategory())
                .amount(categoryExpenseResponse.getAmount())
                .percentage(categoryExpenseResponse.getPercentage())
                .build();
    }

    public static MonthlyYearResponse toMonthlyYearResponse(MonthlyReportResponse monthlyReportResponse) {
        return MonthlyYearResponse.builder()
                .budget(monthlyReportResponse.getBudget())
                .totalExpenses(monthlyReportResponse.getTotalExpenses())
                .netSavings(monthlyReportResponse.getNetSavings())
                .build();
    }

    public static CategoryWiseTopExpense toCategoryWiseTopExpense(ICategoryWiseTopExpense categoryWiseTopExpense) {
        return CategoryWiseTopExpense.builder()
                .category(categoryWiseTopExpense.getCategory())
                .amount(categoryWiseTopExpense.getAmount())
                .percentage(categoryWiseTopExpense.getPercentage())
                .build();
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
