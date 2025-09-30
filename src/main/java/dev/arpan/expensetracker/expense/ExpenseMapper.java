package dev.arpan.expensetracker.expense;

import dev.arpan.expensetracker.expense.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.expense.dto.ExpenseResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/4/25
 */
@Component
public class ExpenseMapper {

    public Expense toExpense(ExpenseRequestDTO expenseRequestDTO) {
        return Expense.builder()
                .name(expenseRequestDTO.getExpenseName())
                .amount(expenseRequestDTO.getAmount())
                .description(expenseRequestDTO.getDescription())
                .paymentMethod(expenseRequestDTO.getPaymentMethod())
                .createdDate(Optional.ofNullable(expenseRequestDTO.getCreatedDate()).orElseGet(LocalDate::now))
                .createdTime(Optional.ofNullable(expenseRequestDTO.getCreatedTime()).orElseGet(LocalTime::now))
                .build();
    }

    public ExpenseResponseDTO toExpenseResponse(Expense expense) {
        return ExpenseResponseDTO.builder()
                .id(expense.getId())
                .expenseName(expense.getName())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .categoryName(expense.getCategory().getName())
                .paymentMethod(expense.getPaymentMethod())
                .createdDate(expense.getCreatedDate())
                .createdTime(expense.getCreatedTime())
                .build();
    }

}
