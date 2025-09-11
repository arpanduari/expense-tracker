package dev.arpan.expensetracker.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Expense Request")
public class ExpenseRequestDTO {
    @Schema(description = "Expense amount", example = "100.00")
    @Positive(message = "Amount must be positive")
    private Double amount;
    @Schema(description = "Expense category id", example = "1")
    private Long categoryId;
    @Schema(description = "Expense description", example = "Food at D Bapi Biryani")
    private String description;

    private LocalDate createdDate;
    private LocalTime createdTime;
}
