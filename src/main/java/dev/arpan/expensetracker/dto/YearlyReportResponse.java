package dev.arpan.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class YearlyReportResponse {
    private Integer year;
    private Map<String, MonthlyYearResponse> monthlyReports;
}
