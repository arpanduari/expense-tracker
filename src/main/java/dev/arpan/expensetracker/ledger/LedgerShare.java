package dev.arpan.expensetracker.ledger;

import dev.arpan.expensetracker.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author arpan
 * @since 10/3/25
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ledger_shares", uniqueConstraints = @UniqueConstraint(columnNames = {"shared_by_user_id", "ledger_user_id"}))
public class LedgerShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_user_id", nullable = false)
    private User sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_user_id", nullable = false)
    private LedgerUser ledgerUser;

    private String token;
}
