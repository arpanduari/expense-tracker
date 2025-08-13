package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Budget Requests")
public class BudgetRequest {
    @Schema(description = "Budget amount of the month", example = "10000.00")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    @Schema(description = "Budget of the month", example = "2025-01")
    private LocalDate month;
}
