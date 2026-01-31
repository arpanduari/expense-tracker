package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Data transfer object for Monthly Report Response")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyReportResponse {
    @Schema(description = "Month", example = "January")
    private String month;
    @Schema(description = "Budget", example = "12000.00")
    private double budget;
    @Schema(description = "Total Expenses", example = "10000.00")
    private double totalExpenses;
    @Schema(description = "Total Savings", example = "2000.00")
    private double netSavings;
}
