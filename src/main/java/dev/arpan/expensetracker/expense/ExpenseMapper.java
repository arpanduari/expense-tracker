package dev.arpan.expensetracker.expense;

import dev.arpan.expensetracker.expense.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.expense.dto.ExpenseResponseDTO;

/**
 * @author arpan
 * @since 8/4/25
 */
public final class ExpenseMapper {
    private ExpenseMapper() {
    }

    public static Expense toExpense(ExpenseRequestDTO expenseRequestDTO) {
        return Expense.builder()
                .amount(expenseRequestDTO.getAmount())
                .description(expenseRequestDTO.getDescription())
                .build();
    }

    public static ExpenseResponseDTO toExpenseResponse(Expense expense) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .categoryName(expense.getCategory().getName())
                .createdDate(expense.getCreatedDate())
                .createdTime(expense.getCreatedTime())
                .build();
    }

}
