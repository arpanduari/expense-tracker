package dev.expensewise.backend.ledgershare;

import dev.expensewise.backend.ledger.LedgerUser;
import dev.expensewise.backend.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

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
@Table(name = "ledger_shares")
public class LedgerShare {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_user_id", nullable = false)
    private User sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_user_id", nullable = false)
    private LedgerUser ledgerUser;

    @Temporal(TemporalType.TIMESTAMP)
    private Instant expiresAt;
}
