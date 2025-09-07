package dev.arpan.expensetracker.budget;

import dev.arpan.expensetracker.budget.dto.BudgetRequest;
import dev.arpan.expensetracker.budget.dto.BudgetResponse;
import dev.arpan.expensetracker.constants.application.ApplicationConstants;
import dev.arpan.expensetracker.exception.AccessDeniedException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author arpan
 * @since 8/6/25
 */
@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserUtil userUtil;

    @Transactional

    public BudgetResponse setDefaultBudget(Long userId, BudgetRequest budgetRequest) {
        User user = userUtil.createUserWithId(userId);
        Budget defaultBudget = budgetRepository.findDefaultBudgetByUserId(userId)
                .orElse(
                        Budget.builder()
                                .user(user)
                                .amount(budgetRequest.amount())
                                .month(null)
                                .updatedAt(LocalDate.now().withDayOfMonth(1))
                                .build()
                );
        if (defaultBudget.getId() != null) {
            List<LocalDate> missingMonths = getMissingMonths(userId, defaultBudget.getUpdatedAt());
            List<Budget> overrides = missingMonths.stream()
                    .map(month -> Budget.builder()
                            .user(user)
                            .amount(defaultBudget.getAmount())
                            .month(month)
                            .build())
                    .collect(Collectors.toList());
            budgetRepository.saveAll(overrides);
        }
        defaultBudget.setAmount(budgetRequest.amount());
        defaultBudget.setMonth(null);
        defaultBudget.setUpdatedAt(LocalDate.now().withDayOfMonth(1));
        Budget updatedBudget = budgetRepository.save(defaultBudget);
        return BudgetMapper.toBudgetResponse(updatedBudget);

    }

    private List<LocalDate> getMissingMonths(Long userId, LocalDate lastUpdatedAt) {
        LocalDate now = LocalDate.now().withDayOfMonth(1);
        LocalDate month = lastUpdatedAt.withDayOfMonth(1);

        Set<LocalDate> overrideMonths = budgetRepository.findAllOverrideBudgetMonths(userId)
                .stream()
                .map(date -> date.withDayOfMonth(1))
                .collect(Collectors.toSet());

        List<LocalDate> missingMonths = new ArrayList<>();

        while (!month.isAfter(now.minusMonths(1))) {
            if (!overrideMonths.contains(month)) {
                missingMonths.add(month);
            }
            month = month.plusMonths(1);
        }
        return missingMonths;
    }


    public BudgetResponse getBudget(Long userId, LocalDate month) {
        if (month != null) {
            Budget budget = budgetRepository.findBudgetByUserIdAndMonth(userId, month)
                    .orElseThrow(() -> new ResourceNotFoundException("Budget", ApplicationConstants.USER_ID, userId + ""));
            return BudgetMapper.toBudgetResponse(budget);
        }
        return getDefaultBudget(userId);
    }


    public BudgetResponse setBudget(Long userId, BudgetRequest budgetRequest) {
        Budget budget = budgetRepository.findBudgetByUserIdAndMonth(userId, budgetRequest.month())
                .orElseGet(
                        () -> {
                            Budget newBudget = BudgetMapper.toBudget(budgetRequest);
                            newBudget.setUser(userUtil.createUserWithId(userId));
                            return newBudget;
                        }
                );
        BudgetMapper.updateBudget(budget, budgetRequest);
        Budget savedBudget = budgetRepository.save(budget);
        return BudgetMapper.toBudgetResponse(savedBudget);
    }


    public BudgetResponse getDefaultBudget(Long userId) {
        Budget budget = budgetRepository.findDefaultBudgetByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default Budget", ApplicationConstants.USER_ID, userId + ""));
        return BudgetMapper.toBudgetResponse(budget);
    }


    public Page<BudgetResponse> getOverrideBudgets(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Budget> budgets = budgetRepository.findAllOverrideBudgets(userId, pageable);
        return budgets.map(BudgetMapper::toBudgetResponse);
    }


    public Page<BudgetResponse> getAllHistoryBudgets(Long userId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Budget> budgetResponses = budgetRepository.findAllByUserId(userId, pageRequest);
        return budgetResponses.map(BudgetMapper::toBudgetResponse);
    }


    public void deleteBudget(Long userId, LocalDate month) {
        Budget budget = budgetRepository.findBudgetByUserIdAndMonth(userId, month)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "userId", userId + ""));
        if (isUserUnAuthorized(userId, budget)) {
            throw new AccessDeniedException("You are not authorized to delete this budget");
        }
        budgetRepository.delete(budget);
    }


    private boolean isUserUnAuthorized(Long userId, Budget budget) {
        return !budget.getUser().getId().equals(userId);
    }
}
