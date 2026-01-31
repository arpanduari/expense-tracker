package dev.expensewise.backend.expense;

import dev.expensewise.backend.constants.application.PageConstants;
import dev.expensewise.backend.expense.dto.ExpenseRequestDTO;
import dev.expensewise.backend.expense.dto.ExpenseResponseDTO;
import dev.expensewise.backend.expense.dto.ExpenseUpdateRequest;
import dev.expensewise.backend.user.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/5/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense Management", description = "Operations related to Expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserUtil userUtil;

    @PostMapping
    @Operation(
            summary = "Create a new expense",
            description = "creates a new expense entry for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Expense created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = ExpenseResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error in request body"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – missing or invalid accessToken"
                    )
            }
    )
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody @Valid ExpenseRequestDTO expenseRequestDTO,
                                                            Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.addExpense(userId, expenseRequestDTO);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get expense by ID",
            description = "Retrieves the details of a specific expense belonging to the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Expense found successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid ID supplied (must be positive)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – missing or invalid accessToken"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden – user not allowed to access this expense"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Expense not found for the given ID"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(
            @PathVariable @Positive Long id,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.getExpenseById(userId, id);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update an existing expense",
            description = "Updates an existing expense belonging to the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Expense updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input (e.g., bad request body or negative ID)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – missing or invalid accessToken"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden – user not allowed to update this expense"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Expense not found for the given ID"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict – update not allowed due to business rule violation"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable @Positive Long id,
                                                            @RequestBody @Valid ExpenseUpdateRequest expenseRequest,
                                                            Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.updateExpense(userId, id, expenseRequest);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @Operation(
            summary = "Delete an expense",
            description = "Deletes an expense for the authenticated user based on the provided expense ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid expense ID supplied"),
                    @ApiResponse(responseCode = "401", description = "User is not authenticated"),
                    @ApiResponse(responseCode = "403", description = "User not authorized to delete this expense"),
                    @ApiResponse(responseCode = "404", description = "Expense not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable @Positive Long id, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        expenseService.deleteExpense(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Get expenses",
            description = "Fetches a paginated list of expenses for the authenticated user. " +
                    "Supports pagination, sorting, and optional filtering by date range and category.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses",
                            content = @Content(schema = @Schema(implementation = ExpenseResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "401", description = "User is not authenticated"),
                    @ApiResponse(responseCode = "403", description = "User not authorized to access these expenses")
            }
    )
    public ResponseEntity<Page<ExpenseResponseDTO>> getExpenses(
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_NUMBER)
            @Min(PageConstants.MIN_PAGE_NUMBER)
            @Parameter(description = "Page number (0 Based)", example = "0")
            int page,
            @Parameter(description = "Number of items to be returned in a page", example = "10")
            @RequestParam(defaultValue = PageConstants.DEFAULT_PAGE_SIZE)
            @Min(PageConstants.MIN_PAGE_SIZE_LIMIT)
            @Max(PageConstants.MAX_PAGE_SIZE_LIMIT)
            int size,
            @Parameter(description = "Sort by field", example = "createdDate")
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String direction,
            @Parameter(description = "Start date filter (YYYY-MM-DD)", example = "2025-01-01")
            @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "End date filter (YYYY-MM-DD)", example = "2025-12-31")
            @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Category ID filter", example = "1")
            @RequestParam(required = false) @Positive Long categoryId,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        Page<ExpenseResponseDTO> expenseResponses = expenseService.getExpenses(userId, page, size, sortBy, direction,
                startDate, endDate, categoryId);
        return ResponseEntity.ok(expenseResponses);
    }
}
