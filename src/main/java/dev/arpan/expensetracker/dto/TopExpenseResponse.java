package dev.arpan.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TopExpenseResponse {
    private String month;
    private Integer year;
    private List<CategoryWiseTopExpense> topExpenses;
}
