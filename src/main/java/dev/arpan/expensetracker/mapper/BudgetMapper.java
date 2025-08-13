package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.BudgetRequest;
import dev.arpan.expensetracker.dto.BudgetResponse;
import dev.arpan.expensetracker.entity.Budget;

/**
 * @author arpan
 * @since 8/6/25
 */
public final class BudgetMapper {
    private BudgetMapper() {
    }
    public static BudgetResponse toBudgetResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .amount(budget.getAmount())
                .month(budget.getMonth())
                .isDefault(budget.getMonth() == null)
                .build();
    }
    public static Budget toBudget(BudgetRequest budgetRequest) {
        return Budget.builder()
                .amount(budgetRequest.getAmount())
                .month(budgetRequest.getMonth())
                .build();
    }
}
