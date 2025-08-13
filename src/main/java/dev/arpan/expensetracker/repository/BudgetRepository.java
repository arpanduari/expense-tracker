package dev.arpan.expensetracker.repository;

import dev.arpan.expensetracker.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/6/25
 */
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query(
            """
                    SELECT b FROM Budget b
                    WHERE b.user.id = :userId
                    AND b.month IS NULL
                    """
    )
    Optional<Budget> findDefaultBudgetByUserId(@Param("userId") Long userId);

    @Query(
            """ 
                     SELECT b FROM Budget b
                     where b.user.id = :userId
                     AND  b.month = :month
                    """
    )
    Optional<Budget> findBudgetByUserIdAndMonth(@Param("userId") Long userId, @Param("month") LocalDate month);

    @Query(
            """
                    SELECT b FROM Budget b
                    WHERE b.user.id = :userId AND b.month IS NOT NULL
                    """
    )
    Page<Budget> findAllOverrideBudgets(@Param("userId") Long userId, Pageable pageable);

    @Query(
            """
                            SELECT b.month FROM Budget b
                            WHERE b.user.id = :userId AND b.month IS NOT NULL
                    """
    )
    List<LocalDate> findAllOverrideBudgetMonths(@Param("userId") Long userId);

    @Query(
            """
                            select b from Budget b
                            WHERE b.user.id = :userId
                    """
    )
    Page<Budget> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query(
            """
                        SELECT MAX(b.month)
                        FROM Budget b
                        where b.user.id = :userId AND b.month IS NOT NULL
                    """
    )
    Optional<LocalDate> findLastOverrideMonth(@Param("userId") Long userId);
}
