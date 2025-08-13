package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.dto.ExpenseResponseDTO;
import dev.arpan.expensetracker.entity.Category;
import dev.arpan.expensetracker.entity.Expense;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.exception.AccessDeniedException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.mapper.ExpenseMapper;
import dev.arpan.expensetracker.repository.CategoryRepository;
import dev.arpan.expensetracker.repository.ExpenseRepository;
import dev.arpan.expensetracker.service.ExpenseService;
import dev.arpan.expensetracker.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 8/4/25
 */
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponseDTO addExpense(Long userId, ExpenseRequestDTO expenseRequestDTO) {
        User user = UserUtil.createUserWithId(userId);
        Expense expense = ExpenseMapper.toExpense(expenseRequestDTO);
        Category category = getCategory(expenseRequestDTO.getCategoryId());
        expense.setCategory(category);
        expense.setUser(user);
        expense.setCreatedDate(LocalDate.now());
        expense.setCreatedTime(LocalTime.now());
        Expense savedExpense = expenseRepository.save(expense);
        if (savedExpense.getId() < 0L) {
            return null;
        }
        return ExpenseMapper.toExpenseResponse(savedExpense);
    }

    @Override
    public ExpenseResponseDTO getExpenseById(Long userId, Long id) {
        User user = UserUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException("You are not authorized to view this expense");
        }
        return ExpenseMapper.toExpenseResponse(expense);
    }

    @Override
    public Page<ExpenseResponseDTO> getExpenses(Long userId, int page, int size, String sortBy, String direction,
                                                LocalDate startDate, LocalDate endDate, Long categoryId) {
        User user = UserUtil.createUserWithId(userId);
        Sort sort = "asc".equalsIgnoreCase(direction) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Expense> expenses = expenseRepository.findExpenseByFilters(user.getId(), startDate, endDate, categoryId, pageable);
        return expenses.map(ExpenseMapper::toExpenseResponse);
    }

    @Override
    public void deleteExpense(Long userId, Long id) {
        User user = UserUtil.createUserWithId(userId);
        Expense expense = getExpense(id);
        if (isUserUnAuthorized(user, expense)) {
            throw new AccessDeniedException("You are not authorized to delete this expense");
        }
        expenseRepository.delete(expense);
    }

    @Override
    public ExpenseResponseDTO updateExpense(Long userId, Long id, ExpenseRequestDTO expenseRequestDTO) {
        User user = UserUtil.createUserWithId(userId);
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
        Expense updatedExpense = expenseRepository.save(expense);
        return ExpenseMapper.toExpenseResponse(updatedExpense);
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
