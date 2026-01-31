package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Insight of Every Month")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsightResponse {
    @Schema(description = "Month", example = "January")
    private String month;

    @Schema(description = "Year", example = "2025")
    private Integer year;

    @Schema(description = "Most Expensive Day of the Month", example = "2025-01-01")
    private LocalDate mostExpensiveDay;

    @Schema(description = "Amount spent on most expensive day", example = "2390.00")
    private double amountOnMostExpensiveDay;

    @Schema(description = "Average Daily Spending", example = "1000.00")
    private double averageDailySpending;

    @Schema(description = "Category with most spending", example = "FOOD")
    private String expensiveCategory;

    @Schema(description = "Amount spent on most expensive category", example = "1000.00")
    private double expensiveCategorySpending;

    @Schema(description = "Total Spending", example = "1000.00")
    private double totalSpending;
}