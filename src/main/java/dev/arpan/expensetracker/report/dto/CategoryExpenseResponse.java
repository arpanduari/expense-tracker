package dev.arpan.expensetracker.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author arpan
 * @since 8/20/25
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryExpenseResponse {
    private String category;
    private double amount;
    private double percentage;
    private String icon;
}
