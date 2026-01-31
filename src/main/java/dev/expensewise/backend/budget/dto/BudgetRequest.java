package dev.expensewise.backend.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
@Schema(description = "Data transfer object for Budget Requests")
public record BudgetRequest(
        @Schema(description = "Budget amount of the month", example = "10000.00")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,
        @Schema(description = "Budget of the month", example = "2025-01")
        LocalDate month) {
}
