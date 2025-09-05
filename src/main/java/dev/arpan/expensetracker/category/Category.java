package dev.arpan.expensetracker.category;

import dev.arpan.expensetracker.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
