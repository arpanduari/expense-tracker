package dev.arpan.expensetracker.expense;

import dev.arpan.expensetracker.category.Category;
import dev.arpan.expensetracker.category.CategoryRepository;
import dev.arpan.expensetracker.exception.AccessDeniedException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.expense.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.expense.dto.ExpenseResponseDTO;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.user.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public ExpenseResponseDTO addExpense(Long userId, ExpenseRequestDTO expenseRequestDTO) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = expenseMapper.toExpense(expenseRequestDTO);
        Category category = getCategory(expenseRequestDTO.getCategoryId());
        expense.setCategory(category);
        expense.setUser(user);
        Expense savedExpense = expenseRepository.save(expense);
        if (savedExpense.getId() < 0L) {
            return null;
        }
        return expenseMapper.toExpenseResponse(savedExpense);
    }


    public ExpenseResponseDTO getExpenseById(Long userId, Long id) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException("You are not authorized to view this expense");
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
            throw new AccessDeniedException("You are not authorized to delete this expense");
        }
        expenseRepository.delete(expense);
    }


    public ExpenseResponseDTO updateExpense(Long userId, Long id, ExpenseRequestDTO expenseRequestDTO) {
        User user = userUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException("You are not authorized to update this expense");
        }
        Category category = getCategory(expenseRequestDTO.getCategoryId());
        expense.setCategory(category);
        if (expenseRequestDTO.getDescription() != null) {
            expense.setDescription(expenseRequestDTO.getDescription());
        }
        if (expenseRequestDTO.getAmount() != null) {
            expense.setAmount(expenseRequestDTO.getAmount());
        }
        if (expenseRequestDTO.getCreatedDate() != null) {
            expense.setCreatedDate(expenseRequestDTO.getCreatedDate());
        }
        if (expenseRequestDTO.getCreatedTime() != null) {
            expense.setCreatedTime(expenseRequestDTO.getCreatedTime());
        }
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
}
