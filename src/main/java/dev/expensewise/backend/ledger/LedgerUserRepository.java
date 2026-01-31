package dev.expensewise.backend.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author arpan
 * @since 9/22/25
 */
@Repository
public interface LedgerUserRepository extends JpaRepository<LedgerUser, Long> {
    boolean existsByIdAndCreatedById(@Param("id") Long id, Long createdBy);
}