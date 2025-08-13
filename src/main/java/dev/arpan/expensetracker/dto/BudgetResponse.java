package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Budget Response")
public class BudgetResponse {
    @Schema(description = "Budget id", example = "1")
    private Long id;
    @Schema(description = "Budget amount", example = "10000.00")
    private BigDecimal amount;
    @Schema(description = "Budget month", example = "2025-01")
    private LocalDate month;
    @Schema(description = "Is budget default", example = "true")
    private boolean isDefault;
}
