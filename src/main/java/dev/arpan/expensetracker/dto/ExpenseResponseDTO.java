package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for Expense Response")
public class ExpenseResponseDTO {
    @Schema(description = "Expense id", example = "1")
    private Long id;
    @Schema(description = "Expense amount", example = "100.00")
    private Double amount;
    @Schema(description = "Expense category id", example = "1")
    private String categoryName;
    @Schema(description = "Expense description", example = "Food at D Bapi Biryani")
    private String description;
    @Schema(description = "Expense created date", example = "2025-08-03")
    private LocalDate createdDate;
    @Schema(description = "Expense created time", example = "12:00:00")
    private LocalTime createdTime;
}
