package dev.expensewise.backend.ledgershare;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author arpan
 * @since 10/3/25
 */
public interface LedgerShareRepository extends JpaRepository<LedgerShare, UUID> {
    @Modifying
    @Transactional
    @Query("""
                DELETE FROM LedgerShare ls
                WHERE ls.expiresAt IS NOT NULL AND
                ls.expiresAt < :now
        """)
    void deleteExpired(Instant now);

    List<LedgerShare> findBySharedByIdAndLedgerUserId(Long sharedBy, Long ledgerUserId);
}
