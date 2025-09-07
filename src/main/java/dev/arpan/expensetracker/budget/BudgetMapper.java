package dev.arpan.expensetracker.budget;

import dev.arpan.expensetracker.budget.dto.BudgetRequest;
import dev.arpan.expensetracker.budget.dto.BudgetResponse;

/**
 * @author arpan
 * @since 8/6/25
 */
public final class BudgetMapper {
    private BudgetMapper() {
    }

    public static BudgetResponse toBudgetResponse(Budget budget) {
        return new BudgetResponse(budget.getId(), budget.getAmount(), budget.getMonth(), budget.getMonth() == null);
    }

    public static Budget toBudget(BudgetRequest budgetRequest) {
        return Budget.builder()
                .amount(budgetRequest.amount())
                .month(budgetRequest.month())
                .build();
    }

    public static void updateBudget(Budget budget, BudgetRequest budgetRequest) {
        budget.setAmount(budgetRequest.amount());
        budget.setMonth(budgetRequest.month());
    }
}
