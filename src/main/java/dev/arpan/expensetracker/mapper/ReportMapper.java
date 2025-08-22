package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.MonthlyReportResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;

import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
public final class ReportMapper {
    private ReportMapper() {
    }

    public static MonthlyReportResponse toMonthlyReportResponse(IMonthlyReportResponse monthlyReportResponse) {
        return MonthlyReportResponse.builder()
                .month(monthlyReportResponse.getMonth())
                .budget(Optional.ofNullable(monthlyReportResponse.getBudget()).orElse(0.0d))
                .netSavings(Optional.ofNullable(monthlyReportResponse.getNetSavings()).orElse(0.0d))
                .totalExpenses(Optional.ofNullable(monthlyReportResponse.getTotalExpenses()).orElse(0.0d))
                .build();
    }
}
