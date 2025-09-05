package dev.arpan.expensetracker.report.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/23/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyYearResponse {
    double budget;
    double totalExpenses;
    double netSavings;
}
