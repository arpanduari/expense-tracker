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
                    
                    SELECT lu.id                                                           AS id,
                           lu.name                                                         AS name,
                           u.secure_url                                                    AS avatarUrl,
                           lu.email                                                        AS email,
                           COALESCE(SUM(IF(le.type = 'credit', le.amount, -le.amount)), 0) AS totalAmount,
                           MAX(COALESCE(le.updated_at, lu.created_at))                   AS last_updated
                    FROM ledger_user lu
                             LEFT JOIN user u ON lu.email = u.email 
                             LEFT JOIN ledger_entry le ON lu.id = le.ledger_user_id
                        where lu.created_by = :userId
                    GROUP BY lu.id, lu.name, lu.email, u.secure_url;
                    """, nativeQuery = true
    )
    List<ILedgerUserEntryDetails> findLedgerUserEntryDetailsByUserId(@Param("userId") Long userId);
}
