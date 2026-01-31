package dev.expensewise.backend.expense.dto;

import dev.expensewise.backend.expense.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 10/1/25
 */
@Schema(description = "Data transfer object for Expense Update Request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseUpdateRequest {
    @Schema(description = "Expense amount", example = "100.00")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @Schema(description = "Expense name", example = "Biryani")
    private String expenseName;

    @Schema(description = "Expense description", example = "Food at D Bapi Biryani")
    private String description;

    @Schema(description = "Expense category id", example = "1")
    private Long categoryId;

    @Schema(description = "Expense Payment Method", example = "CASH")
    private PaymentMethod paymentMethod;

    @Schema(description = "Expense created date", example = "2025-08-03")
    private LocalDate createdDate;

    @Schema(description = "Expense created time", example = "12:00:00")
    private LocalTime createdTime;
}
