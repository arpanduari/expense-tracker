package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.CategoryRequest;
import dev.arpan.expensetracker.dto.CategoryResponse;
import dev.arpan.expensetracker.entity.Category;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.exception.AccessDeniedException;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.mapper.CategoryMapper;
import dev.arpan.expensetracker.repository.CategoryRepository;
import dev.arpan.expensetracker.service.CategoryService;
import dev.arpan.expensetracker.utils.UserUtil;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserUtil userUtil;

    @Override
    public Page<CategoryResponse> getCategories(Long userId, int page, int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<Category> categories = categoryRepository.findByUserId(userId, pageable);
        return categories.map(category -> CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build()
        );
    }

    @Override
    public CategoryResponse createCategory(Long userId, CategoryRequest categoryRequest) {
        User user = userUtil.createUserWithId(userId);
        Category newCategory = CategoryMapper.toCategory(categoryRequest);
        newCategory.setUser(user);
        Category savedCategory = categoryRepository.save(newCategory);
        if (savedCategory != null && savedCategory.getId() < 0L) {
            return null;
        }
        return CategoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long userId, Long id, CategoryRequest categoryRequest) {
        User user = userUtil.createUserWithId(userId);
        Category category = getCategory(id);
        checkCategoryByUser(user, category);
        category.setName(categoryRequest.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
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
