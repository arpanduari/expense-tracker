package dev.expensewise.backend.expense;

import dev.expensewise.backend.category.Category;
import dev.expensewise.backend.category.CategoryRepository;
import dev.expensewise.backend.exception.AccessDeniedException;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.expense.dto.ExpenseRequestDTO;
import dev.expensewise.backend.expense.dto.ExpenseResponseDTO;
import dev.expensewise.backend.expense.dto.ExpenseUpdateRequest;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author arpan
 * @since 8/4/25
 */
@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final UserUtil userUtil;
    private final ExpenseMapper expenseMapper;
    private final MessageSource messageSource;

    public ExpenseResponseDTO addExpense(Long userId, ExpenseRequestDTO expenseRequestDTO) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = expenseMapper.toExpense(expenseRequestDTO);
        Category category = getCategory(expenseRequestDTO.getCategoryId());
        expense.setCategory(category);
        expense.setUser(user);
        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponse(savedExpense);
    }


    public ExpenseResponseDTO getExpenseById(Long userId, Long id) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException(messageSource.getMessage("expense.unauthorized.view", null, getLocale()));
        }
        return expenseMapper.toExpenseResponse(expense);
    }


    public Page<ExpenseResponseDTO> getExpenses(Long userId, int page, int size, String sortBy, String direction,
                                                LocalDate startDate, LocalDate endDate, Long categoryId) {
        Sort sort = "asc".equalsIgnoreCase(direction) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Expense> expenses = expenseRepository.findExpenseByFilters(userId, startDate, endDate, categoryId, pageable);
        return expenses.map(expenseMapper::toExpenseResponse);
    }


    public void deleteExpense(Long userId, Long id) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException(messageSource.getMessage("expense.unauthorized.delete", null, getLocale()));
        }
        expenseRepository.delete(expense);
    }


    public ExpenseResponseDTO updateExpense(Long userId, Long id, ExpenseUpdateRequest expenseUpdateRequest) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException(messageSource.getMessage("expense.unauthorized.update", null, getLocale()));
        }
        if (expenseUpdateRequest.getCategoryId() != null) {
            Category category = getCategory(expenseUpdateRequest.getCategoryId());
            expense.setCategory(category);
        }
        setIfNotNull(expense::setName, expenseUpdateRequest.getExpenseName());
        setIfNotNull(expense::setDescription, expenseUpdateRequest.getDescription());
        setIfNotNull(expense::setAmount, expenseUpdateRequest.getAmount());
        setIfNotNull(expense::setCreatedDate, expenseUpdateRequest.getCreatedDate());
        setIfNotNull(expense::setCreatedTime, expenseUpdateRequest.getCreatedTime());
        setIfNotNull(expense::setPaymentMethod, expenseUpdateRequest.getPaymentMethod());
        Expense updatedExpense = expenseRepository.save(expense);
        return expenseMapper.toExpenseResponse(updatedExpense);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id + ""));
    }

    public Expense getExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id.toString()));
    }

    public boolean isUserUnAuthorized(User user, Expense expense) {
        return !expense.getUser().getId().equals(user.getId());
    }

    private <T> void setIfNotNull(Consumer<T> setter, T data) {
        Optional.ofNullable(data).ifPresent(setter);
    }

    private Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
