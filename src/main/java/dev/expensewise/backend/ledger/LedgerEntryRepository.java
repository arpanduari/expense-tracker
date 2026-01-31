package dev.expensewise.backend.ledger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author arpan
 * @since 9/22/25
 */
@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByLedgerUser_IdOrderByUpdatedAtDescCreatedDateDesc(@Param("ledgerUserID") Long ledgerUserId);

    boolean existsByLedgerUser_CreatedBy_IdAndId(Long createdById, Long ledgerEntryId);

    Page<LedgerEntry> findByLedgerUser_CreatedBy_Id(Long userId, Pageable pageable);
}
