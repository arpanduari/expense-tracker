package dev.arpan.expensetracker.report.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 9/11/25
 */
public record DailyExpenseResponse(Long id, Double amount, String category, String description, LocalDate createdDate,
                                   LocalTime createdAtTime) {
}
