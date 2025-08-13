package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.CategoryRequest;
import dev.arpan.expensetracker.dto.CategoryResponse;
import dev.arpan.expensetracker.service.CategoryService;
import dev.arpan.expensetracker.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author arpan
 * @since 8/3/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                Authentication authentication
    ) {
        Long userId = UserUtil.getUserId(authentication);
        Page<CategoryResponse> categories = categoryService.getCategories(userId, page, size);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest,
                                                           Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        CategoryResponse categoryResponse = categoryService.createCategory(userId, categoryRequest);
        if (categoryResponse == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @RequestBody CategoryRequest categoryRequest,
                                                           Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        CategoryResponse categoryResponse = categoryService.updateCategory(userId, id, categoryRequest);
        if (categoryResponse == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(categoryResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }
}
