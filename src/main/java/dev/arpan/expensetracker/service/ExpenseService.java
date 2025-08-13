package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.dto.ExpenseResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/2/25
 */
public interface ExpenseService {
    ExpenseResponseDTO addExpense(Long userId, ExpenseRequestDTO expenseRequestDTO);

    ExpenseResponseDTO getExpenseById(Long userId, Long id);

    Page<ExpenseResponseDTO> getExpenses(Long userId, int page, int size, String sortBy,
                                         String direction, LocalDate startDate, LocalDate endDate,
                                         Long categoryId);

    void deleteExpense(Long userId, Long id);
    ExpenseResponseDTO updateExpense(Long userId, Long id, ExpenseRequestDTO expenseRequestDTO);
}
