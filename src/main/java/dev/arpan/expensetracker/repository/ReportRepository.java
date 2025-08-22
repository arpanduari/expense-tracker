package dev.arpan.expensetracker.repository;

import dev.arpan.expensetracker.entity.DummyEntity;
import dev.arpan.expensetracker.projection.ICategoryExpenseResponse;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
@Repository
public interface ReportRepository extends JpaRepository<DummyEntity, Long> {
    @Query(
            value = """ 
                    select
                               :startDate as month,
                               coalesce(mb.budget, 0) as budget,
                               coalesce(me.totalExpenses, 0) as totalExpenses,
                               coalesce(mb.budget, 0) - coalesce(me.totalExpenses, 0) as netSavings
                           from
                           (
                               select
                                   b.amount as budget
                               from
                                   budget b
                               where
                                   b.user_id = :userId
                                   and (
                                       b.month = :startDate
                                       or b.month is null
                                   )
                               order by
                                   (b.month is null),
                                   b.month desc
                               limit 1
                           ) mb
                           cross join
                           (
                               select
                                   sum(e.amount) as totalExpenses
                               from
                                   expense e
                               where
                                   e.user_id = :userId
                                   and e.created_date BETWEEN :startDate AND :endDate
                           ) me
                    """, nativeQuery = true
    )
    Optional<IMonthlyReportResponse> findMonthlyReport(Long userId, LocalDate startDate, LocalDate endDate);

    @Query(
            value = """
                            select c.name as category,
                            sum(e.amount) as amount,
                            round(sum(e.amount) / b.amount * 100 ) as percentage
                           from expense e
                           join category c on c.id = e.category_id and c.user_id = :userId
                           join(
                            select b.amount
                            from budget b
                            where b.user_id = :userId
                            AND (month(b.month) = month(:startDate) or b.month is null)
                            order by (b.month is null), b.month desc
                            limit 1
                           ) b
                           on true
                           group by c.id, c.name
                    """, nativeQuery = true
    )
    List<ICategoryExpenseResponse> findCategoryExpenseByUserId(Long userId, LocalDate startDate, LocalDate endDate);
}
