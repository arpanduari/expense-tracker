package dev.arpan.expensetracker.budget;

import dev.arpan.expensetracker.budget.dto.BudgetRequest;
import dev.arpan.expensetracker.budget.dto.BudgetResponse;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 8/6/25
 */
@Component
public class BudgetMapper {

    public BudgetResponse toBudgetResponse(Budget budget) {
        return new BudgetResponse(budget.getId(), budget.getAmount(), budget.getMonth(), budget.getMonth() == null);
    }

    public Budget toBudget(BudgetRequest budgetRequest) {
        return Budget.builder()
                .amount(budgetRequest.amount())
                .month(budgetRequest.month())
                .build();
    }

    public void updateBudget(Budget budget, BudgetRequest budgetRequest) {
        budget.setAmount(budgetRequest.amount());
        budget.setMonth(budgetRequest.month());
    }
}
