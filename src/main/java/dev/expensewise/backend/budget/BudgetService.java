package dev.expensewise.backend.budget;

import dev.expensewise.backend.budget.dto.BudgetRequest;
import dev.expensewise.backend.budget.dto.BudgetResponse;
import dev.expensewise.backend.constants.application.ApplicationConstants;
import dev.expensewise.backend.exception.AccessDeniedException;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private final BudgetMapper budgetMapper;
    private final MessageSource messageSource;

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
        return budgetMapper.toBudgetResponse(updatedBudget);

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
            return budgetMapper.toBudgetResponse(budget);
        }
        return getDefaultBudget(userId);
    }


    public BudgetResponse setBudget(Long userId, BudgetRequest budgetRequest) {
        Budget budget = budgetRepository.findBudgetByUserIdAndMonth(userId, budgetRequest.month())
                .orElseGet(
                        () -> {
                            Budget newBudget = budgetMapper.toBudget(budgetRequest);
                            newBudget.setUser(userUtil.createUserWithId(userId));
                            return newBudget;
                        }
                );
        budgetMapper.updateBudget(budget, budgetRequest);
        Budget savedBudget = budgetRepository.save(budget);
        return budgetMapper.toBudgetResponse(savedBudget);
    }


    public BudgetResponse getDefaultBudget(Long userId) {
        Budget budget = budgetRepository.findDefaultBudgetByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default Budget", ApplicationConstants.USER_ID, userId + ""));
        return budgetMapper.toBudgetResponse(budget);
    }


    public Page<BudgetResponse> getOverrideBudgets(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Budget> budgets = budgetRepository.findAllOverrideBudgets(userId, pageable);
        return budgets.map(budgetMapper::toBudgetResponse);
    }


    public Page<BudgetResponse> getAllHistoryBudgets(Long userId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Budget> budgetResponses = budgetRepository.findAllByUserId(userId, pageRequest);
        return budgetResponses.map(budgetMapper::toBudgetResponse);
    }

    @Transactional
    public void deleteBudget(Long userId, LocalDate month) {
        Budget budget = budgetRepository.findBudgetByUserIdAndMonth(userId, month)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "userId", userId + ""));
        if (isUserUnAuthorized(userId, budget)) {
            throw new AccessDeniedException(messageSource.getMessage("budget.unauthorized.delete", null, getLocale()));
        }
        budgetRepository.delete(budget);
    }


    private boolean isUserUnAuthorized(Long userId, Budget budget) {
        return !budget.getUser().getId().equals(userId);
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
