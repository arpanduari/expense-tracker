package dev.arpan.expensetracker.mapper;

import dev.arpan.expensetracker.dto.CategoryRequest;
import dev.arpan.expensetracker.dto.CategoryResponse;
import dev.arpan.expensetracker.entity.Category;

/**
 * @author arpan
 * @since 8/3/25
 */
public final class CategoryMapper {
    private CategoryMapper() {
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .build();
    }

    public static Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.getName().toUpperCase())
                .build();
    }
}
