package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Category wise monthly expense response")
public record CategoryWiseMonthlyExpenseResponse(
        @Schema(description = "Month", example = "September-2025")
        String month,
        @Schema(description = "Category wise expense response")
        List<CategoryExpenseResponse> categoryWiseExpenses) {
}

