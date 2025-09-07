package dev.arpan.expensetracker.report.dto;

import java.util.List;

/**
 * @author arpan
 * @since 8/20/25
 */

public record CategoryWiseMonthlyExpenseResponse(
        String month,
        List<CategoryExpenseResponse> categoryWiseExpenses) {
}

