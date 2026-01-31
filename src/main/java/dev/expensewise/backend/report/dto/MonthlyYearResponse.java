package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/23/25
 */
@Schema(description = "Data transfer object for Monthly year response")
public record MonthlyYearResponse(
        @Schema(description = "Budget of the month", example = "12000.00")
        double budget,
        @Schema(description = "Total expenses of the month", example = "10000.00")
        double totalExpenses,
        @Schema(description = "Total savings of the month", example = "2000.00")
        double netSavings) {
}
