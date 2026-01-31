package dev.expensewise.backend.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * @author arpan
 * @since 8/20/25
 */
@Schema(description = "Yearly report response")
public record YearlyReportResponse(
        @Schema(description = "Year", example = "2025")
        Integer year,
        @Schema(description = "Month wise report response")
        Map<String, MonthlyYearResponse> monthlyReports) {
}
