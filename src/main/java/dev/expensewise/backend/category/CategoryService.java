package dev.expensewise.backend.category;

import dev.expensewise.backend.category.dto.CategoryRequest;
import dev.expensewise.backend.category.dto.CategoryResponse;
import dev.expensewise.backend.exception.AccessDeniedException;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author arpan
 * @since 8/3/25
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserUtil userUtil;
    private final EmojiService emojiService;
    private final CategoryMapper categoryMapper;

    public Page<CategoryResponse> getCategories(Long userId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Category> categories = categoryRepository.findByUserId(userId, pageable);
        return categories.map(categoryMapper::toCategoryResponse);
    }


    public CategoryResponse createCategory(Long userId, CategoryRequest categoryRequest) {
        User user = userUtil.createUserWithId(userId);
        Category newCategory = categoryMapper.toCategory(categoryRequest);
        newCategory.setUser(user);
        newCategory.setIcon(emojiService.getCategory(newCategory.getName()));
        Category savedCategory = categoryRepository.save(newCategory);
        if (savedCategory != null && savedCategory.getId() < 0L) {
            return null;
        }
        return categoryMapper.toCategoryResponse(savedCategory);
    }


    public CategoryResponse updateCategory(Long userId, Long id, CategoryRequest categoryRequest) {
        User user = userUtil.createUserWithId(userId);
        Category category = getCategory(id);
        checkCategoryByUser(user, category);
        category.setName(categoryRequest.name());
        category.setIcon(categoryRequest.icon());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(savedCategory);
    }


    public void deleteCategory(Long userId, Long id) {
        User user = userUtil.createUserWithId(userId);
        Category category = getCategory(id);
        checkCategoryByUser(user, category);
        categoryRepository.delete(category);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id + ""));
    }

    public void checkCategoryByUser(User user, Category category) {
        if (!category.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this category");
        }
    }
}
