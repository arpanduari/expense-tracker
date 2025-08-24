package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 8/23/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWiseTopExpense {
    private String category;
    private double amount;
    private double percentage;
}
