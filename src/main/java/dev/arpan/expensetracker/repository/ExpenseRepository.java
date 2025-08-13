package dev.arpan.expensetracker.repository;

import dev.arpan.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/5/25
 */
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(
            """
                            SELECT e FROM Expense e
                            WHERE e.user.id = :userId
                            AND (:startDate IS NULL OR e.createdDate >= :startDate)
                            AND (:endDate IS NULL OR e.createdDate <= :endDate)
                            AND (:categoryId IS NULL OR e.category.id = :categoryId)
                    """
    )
    Page<Expense> findExpenseByFilters(@Param("userId") Long userId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       @Param("categoryId") Long categoryId, Pageable pageable);
}
