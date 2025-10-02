package dev.arpan.expensetracker.ledger;

import dev.arpan.expensetracker.projection.ILedgerUserEntryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> {
    @Query(
            value = """ 
                    SELECT lu.id   AS id,
                           lu.name AS name,
                           lu.email AS email,
                           COALESCE(SUM(
                                   IF(le.type = 'credit', le.amount, -le.amount)
                           )       , 0 )AS totalAmount,
                           COALESCE(MAX(
                                   COALESCE(le.updated_at, le.created_date)
                       ), lu.created_at)AS lastUpdated
                    FROM ledger_user lu
                             LEFT JOIN ledger_entry le ON le.ledger_user_id = lu.id
                    WHERE lu.created_by = :userId
                    GROUP BY lu.id, lu.name
                    """, nativeQuery = true
    )
    List<ILedgerUserEntryDetails> findLedgerUserEntryDetailsByUserId(@Param("userId") Long userId);
}
