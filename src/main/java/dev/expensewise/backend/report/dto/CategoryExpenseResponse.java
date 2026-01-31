package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Data transfer object for Category wise expense response")
public record CategoryExpenseResponse(
        @Schema(description = "Category", example = "Food")
        String category,
        @Schema(description = "Amount", example = "10000.00")
        double amount,
        @Schema(description = "Percentage", example = "10.00")
        double percentage,
        @Schema(description = "Icon", example = "\uD83C\uDF72")
        String icon) {
}
