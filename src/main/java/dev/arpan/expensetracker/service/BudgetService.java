package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.BudgetRequest;
import dev.arpan.expensetracker.dto.BudgetResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
public interface BudgetService {
    BudgetResponse setDefaultBudget(Long userId, BudgetRequest budgetRequest);

    BudgetResponse getBudget(Long userId, LocalDate month);

    BudgetResponse setBudget(Long userId, BudgetRequest budgetRequest);

    BudgetResponse getDefaultBudget(Long userId);

    Page<BudgetResponse> getOverrideBudgets(Long userId, int page, int size);

    Page<BudgetResponse> getAllHistoryBudgets(Long userId, int page, int size);

    void deleteBudget(Long userId, LocalDate month);
}
