package dev.expensewise.backend.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
@Schema(description = "Data transfer object for Budget Response")
public record BudgetResponse(
        @Schema(description = "Budget id", example = "1")
        Long id,
        @Schema(description = "Budget amount", example = "10000.00")
        BigDecimal amount,
        @Schema(description = "Budget month", example = "2025-01")
        LocalDate month,
        @Schema(description = "Is budget default", example = "true")
        boolean isDefault) {
}
