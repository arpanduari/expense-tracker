package dev.arpan.expensetracker.repository;

import dev.arpan.expensetracker.entity.DummyEntity;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
public interface ReportRepository extends JpaRepository<DummyEntity, Long> {
    @Query(
            value = """
                                select :month as month, mb.budget, me.totalExpense, mb.budget - me.totalExpense
                                from
                                   (
                                      select
                                         b.amount as budget
                                      from
                                         Budget b
                                      where
                                         b.user_id = :userId
                                         AND coalesce(b.month, :month) = :month
                                   )
                                   as mb,
                                   (
                                      select
                                         sum(e.amount) as totalExpense
                                      from
                                         Expense e
                                      where
                                         e.user_id = :userId
                                         AND month(e.created_date) = month(:month)
                                   )
                                   as me
                    """, nativeQuery = true
    )
    Optional<IMonthlyReportResponse> findMonthlyReport(Long userId, LocalDate month);

}
