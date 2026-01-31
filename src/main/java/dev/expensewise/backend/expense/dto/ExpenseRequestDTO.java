package dev.expensewise.backend.expense.dto;

import dev.expensewise.backend.expense.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "Expense name", example = "Biryani")
    @NotBlank(message = "Name cannot be blank")
    private String expenseName;

    @Schema(description = "Expense category id", example = "1")
    @NotNull(message = "Category cannot be null")
    private Long categoryId;

    @Schema(description = "Expense description", example = "Food at D Bapi Biryani")
    private String description;

    @Schema(description = "Expense Payment Method", example = "CASH")
    @NotNull
    private PaymentMethod paymentMethod;

    @Schema(description = "Expense created date", example = "2025-08-03")
    private LocalDate createdDate;

    @Schema(description = "Expense created time", example = "12:00:00")
    private LocalTime createdTime;
}
