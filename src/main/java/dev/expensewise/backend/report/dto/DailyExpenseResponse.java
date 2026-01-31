package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 9/11/25
 */
@Schema(description = "Daily expense response")
public record DailyExpenseResponse(
        @Schema(description = "Expense id", example = "1")
        Long id,
        @Schema(description = "Expense Name", example = "Biryani")
        String expenseName,
        @Schema(description = "Expense amount", example = "10000.00")
        Double amount,
        @Schema(description = "Expense category", example = "FOOD")
        String category,
        @Schema(description = "Expense description", example = "Food at D Bapi Biryani")
        String description,
        @Schema(description = "Expense created date", example = "2025-08-03")
        LocalDate createdDate,
        @Schema(description = "Expense created time", example = "12:00:00")
        LocalTime createdAtTime) {
}
