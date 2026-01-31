package dev.expensewise.backend.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author arpan
 * @since 8/3/25
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByUserId(Long userId, Pageable pageable);
}
