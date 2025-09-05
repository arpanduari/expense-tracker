package dev.arpan.expensetracker.expense;

import dev.arpan.expensetracker.category.Category;
import dev.arpan.expensetracker.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    private double amount;
    @Column(updatable = false)
    private LocalDate createdDate;
    private LocalTime createdTime;
    private String description;
}
