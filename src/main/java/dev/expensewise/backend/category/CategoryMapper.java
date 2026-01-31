package dev.expensewise.backend.category;

import dev.expensewise.backend.category.dto.CategoryRequest;
import dev.expensewise.backend.category.dto.CategoryResponse;
import org.springframework.stereotype.Component;

/**
 * @author arpan
 * @since 8/3/25
 */
@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getIcon());
    }

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name().toUpperCase())
                .build();
    }
}
