package dev.arpan.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BudgetVsActualResponse {
    private String month;
    private double budgetAmount;
    private double actualExpenses;
    private double remainingBudget;
    private String status;

}
