package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.MonthlyReportResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;

/**
 * @author arpan
 * @since 8/20/25
 */
public final class ReportMapper {
    private ReportMapper(){
    }
    public static MonthlyReportResponse toMonthlyReportResponse(IMonthlyReportResponse monthlyReportResponse){
        return MonthlyReportResponse.builder()
                .month(monthlyReportResponse.getMonth())
                .budget(monthlyReportResponse.getBudget())
                .netSavings(monthlyReportResponse.getNetSavings())
                .totalExpenses(monthlyReportResponse.getTotalExpenses())

                .build();
    }
}
