package dev.arpan.expensetracker.ledger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author arpan
 * @since 10/3/25
 */
public interface LedgerShareRepository extends JpaRepository<LedgerShare, Long> {
    Optional<LedgerShare> findByToken(String token);

    Optional<LedgerShare> findBySharedByIdAndLedgerUserId(Long userId, Long ledgerUserId);
}