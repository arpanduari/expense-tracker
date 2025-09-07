package dev.arpan.expensetracker.category;

import dev.arpan.expensetracker.category.dto.CategoryRequest;
import dev.arpan.expensetracker.category.dto.CategoryResponse;

/**
 * @author arpan
 * @since 8/3/25
 */
public final class CategoryMapper {
    private CategoryMapper() {
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getIcon());
    }

    public static Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name().toUpperCase())
                .build();
    }
}
