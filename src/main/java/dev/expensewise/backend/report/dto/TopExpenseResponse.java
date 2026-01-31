package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Top expense response")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TopExpenseResponse {
    @Schema(description = "Month", example = "January")
    private String month;
    @Schema(description = "Year", example = "2025")
    private Integer year;
    @Schema(description = "Category Wise Top expense response")
    private List<CategoryWiseTopExpenseResponse> topExpenses;
}
