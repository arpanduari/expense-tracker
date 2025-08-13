package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.CategoryRequest;
import dev.arpan.expensetracker.dto.CategoryResponse;
import org.springframework.data.domain.Page;

/**
 * @author arpan
 * @since 8/3/25
 */
public interface CategoryService {
    Page<CategoryResponse> getCategories(Long userId, int page, int size);
    CategoryResponse createCategory(Long userId, CategoryRequest categoryRequest);
    CategoryResponse updateCategory(Long userId, Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long userId, Long id);
}
