package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyReportResponse {
    private String month;
    private double budget;
    private double totalExpenses;
    private double netSavings;
}
