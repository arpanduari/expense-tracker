package dev.arpan.expensetracker.expense;

import dev.arpan.expensetracker.category.Category;
import dev.arpan.expensetracker.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate createdDate;

    private LocalTime createdTime;

    private String description;
}
