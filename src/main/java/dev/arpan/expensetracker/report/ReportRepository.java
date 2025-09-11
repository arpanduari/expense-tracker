package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.expense.Expense;
import dev.arpan.expensetracker.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author arpan
 * @since 8/20/25
 */
@Repository
public interface ReportRepository extends JpaRepository<Expense, Long> {
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
                                       month(b.month) = month(:startDate)
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
                        with budget_cte as (
                            select b.amount
                            from budget b
                            where b.user_id = :userId
                            AND ((month(b.month) = month(:startDate) and year(b.month) = year(:startDate))or  b.month is null)
                            order by (b.month is null), b.month desc
                            limit 1
                        ),
                        category_expense as (
                            select e.category_id,
                            sum(e.amount) as total_amount
                            from expense e
                            where e.user_id = :userId
                            and month(e.created_date) = month(:startDate)
                            and year(e.created_date) = year(:startDate)
                            group by e.category_id
                        )
                        select c.name as category,
                        ce.total_amount as amount,
                        round(ce.total_amount / b.amount * 100, 2) as percentage,
                        c.icon as icon
                        from category_expense ce
                        join category c on c.id = ce.category_id and c.user_id = :userId
                        join budget_cte b on true
                        order by c.name
                    """, nativeQuery = true
    )
    List<ICategoryExpenseResponse> findCategoryExpenseByUserId(Long userId, LocalDate startDate, LocalDate endDate);

    @Query(
            value = """
                    WITH budget_cte AS
                     (SELECT b.amount AS budget
                      FROM budget b
                      WHERE b.user_id = :userId
                        AND (b.month = :startDate
                             OR b.month IS NULL)
                      ORDER BY (b.month IS NULL), b.month DESC
                      LIMIT 1)
                    SELECT c.name AS category,
                          sum(e.amount) AS amount,
                          round(sum(e.amount) / bc.budget * 100, 2) AS percentage,
                          c.icon as icon
                    FROM budget_cte bc
                    JOIN expense e ON e.created_date BETWEEN :startDate AND :endDate
                    JOIN category c ON c.id = e.category_id
                    WHERE c.user_id = :userId
                    GROUP BY c.name,
                            bc.budget, c.icon
                    ORDER BY amount DESC
                    LIMIT :limit
                    """, nativeQuery = true
    )
    List<ICategoryWiseTopExpense> findCategoryWiseTopExpense(Long userId, Integer limit, LocalDate startDate, LocalDate endDate);

    @Query(
            value = """
                    with expensive_day_cte as (
                        select e.created_date as mostExpensiveday,
                        sum(e.amount) amountOnMostExpensiveDay
                        from expense e
                        where e.user_id = :userId
                        and e.created_date between :startDate AND :endDate
                        group by e.created_date
                        order by amountOnMostExpensiveDay desc
                        limit 1
                    ),
                    average_spending_cte as(
                        select round(sum(e.amount) / day(last_day(:startDate)), 2) as averageDailySpending
                        from expense e
                        where e.user_id = :userId
                        and e.created_date between :startDate and :endDate
                    ),
                    most_expensive_category as(
                        select c.name as categoryName, sum(e.amount) as categorySpending
                        from expense e
                        join category c on c.id = e.category_id
                        where e.user_id = :userId
                        and e.created_date between :startDate and :endDate
                        group by c.name
                        order by categorySpending desc
                        limit 1
                    ),
                    total_month_spending as(
                        select sum(e.amount) as totalSpending
                        from expense e
                        where e.user_id = :userId
                        and month(e.created_date) = month(:startDate)
                        and year(e.created_date) = year(:startDate)
                    )
                    select ed.mostExpensiveday as mostExpensiveDay,
                    ed.amountOnMostExpensiveDay as amountOnMostExpensiveDay,
                    av.averageDailySpending as averageDailySpending,
                    mc.categoryName as expensiveCategory,
                    mc.categorySpending as expensiveCategorySpending,
                    ts.totalSpending as totalSpending
                    from expensive_day_cte ed,
                         average_spending_cte av,
                         most_expensive_category mc,
                         total_month_spending ts
                    """
            , nativeQuery = true
    )
    Optional<IInsightResponse> findInsight(Long userId, LocalDate startDate, LocalDate endDate);
    @Query(
            """
                        select e.amount as amount, e.category.name as category,
                        e.description as description, e.createdDate as createdDate,
                        e.createdTime as createdTime
                        from Expense e
                        where e.user.id = :userId
                        AND e.createdDate between :startDate AND :endDate
                    """
    )
    List<IDailyExpense> findExpenseInRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}