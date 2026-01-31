package dev.expensewise.backend.category;

import dev.expensewise.backend.category.dto.CategoryRequest;
import dev.expensewise.backend.category.dto.CategoryResponse;
import dev.expensewise.backend.constants.application.PageConstants;
import dev.expensewise.backend.user.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
@Tag(name = "Category Management", description = "Operations related to Category")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserUtil userUtil;
    
    @GetMapping
    @Operation(
            summary = "Get categories of the logged in user",
            description = "Fetches a CategoryResponse Object from the database. With a default page value 0 & page size 10",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Categories created by the logged in user",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = CategoryResponse.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Page<CategoryResponse>> getCategories(
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_NUMBER)
            @Min(PageConstants.MIN_PAGE_NUMBER)
            int page,
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_SIZE)
            @Min(PageConstants.MIN_PAGE_SIZE_LIMIT)
            @Max(PageConstants.MAX_PAGE_SIZE_LIMIT)
            int size,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        Page<CategoryResponse> categories = categoryService.getCategories(userId, page, size);
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @Operation(
            summary = "Create a new Category for the logged in user",
            description = """
                    Creates a new category associated with the authenticated user. Requires a valid authentication accessToken.
                    Returns the created CategoryResponse object on success.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Category created successfully.",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = CategoryResponse.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                           Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        CategoryResponse categoryResponse = categoryService.createCategory(userId, categoryRequest);
        if (categoryResponse == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing category",
            description = "Updates the details of an existing category belonging to the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Returns the updated CategoryResponse object on success.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Category updated successfully",
                            content = @Content(
                                    schema = @Schema(implementation = CategoryResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request (e.g., missing or malformed data)",
                            content = @Content(
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found for the given id",
                            content = @Content(
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    )
            }
    )
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable @Positive Long id,
                                                           @RequestBody @Valid CategoryRequest categoryRequest,
                                                           Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        CategoryResponse categoryResponse = categoryService.updateCategory(userId, id, categoryRequest);
        if (categoryResponse == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(categoryResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a category",
            description = "Deletes an existing category belonging to the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Returns no content on successful deletion.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Category deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category not found for the given id"
                    )
            }
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long id, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        categoryService.deleteCategory(userId, id);
        return ResponseEntity.noContent().build();
    }
}
