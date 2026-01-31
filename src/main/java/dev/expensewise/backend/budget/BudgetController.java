package dev.expensewise.backend.budget;

import dev.expensewise.backend.budget.dto.BudgetRequest;
import dev.expensewise.backend.budget.dto.BudgetResponse;
import dev.expensewise.backend.constants.application.PageConstants;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/6/25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base}${api.version}/budgets")
@Tag(name = "Budget Management", description = "Operations related to Budget")
public class BudgetController {
    private final BudgetService budgetService;
    private final UserUtil userUtil;

    @GetMapping
    @Operation(
            summary = "Get budget for the current month or Get the default budget",
            description = "Fetches a user BudgetResponse Object from the database using the provided month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BudgetResponse object for the provided month",
                            content = @Content(schema = @Schema(implementation = BudgetResponse.class))
                    )
            }
    )
    public ResponseEntity<BudgetResponse> getBudget(
            @RequestParam(required = false, value = "month")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(
                    name = "month",
                    description = "Any ISO date within the target month",
                    example = "2025-01-01"
            )
            LocalDate month,
            Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.getBudget(userId, month);
        return ResponseEntity.ok(budgetResponse);
    }

    @PostMapping("/default")
    @Operation(
            summary = "Set a default budget for the logged-in user",
            description = "Creates or updates the default budget for the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Returns the BudgetResponse object representing the default budget.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Default budget set successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request (e.g., missing or malformed data)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    )
            }
    )
    public ResponseEntity<BudgetResponse> setDefaultBudget(@RequestBody @Valid BudgetRequest budgetRequest,
                                                           Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.setDefaultBudget(userId, budgetRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(budgetResponse);
    }

    @GetMapping("/default")
    @Operation(
            summary = "Get the default budget of the logged-in user",
            description = "Fetches the default budget associated with the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Returns the BudgetResponse object if a default budget is set.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Default budget retrieved successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No default budget found for the user"
                    )
            }
    )
    public ResponseEntity<BudgetResponse> getDefaultBudget(Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        BudgetResponse defaultBudget = budgetService.getDefaultBudget(userId);
        return ResponseEntity.ok(defaultBudget);
    }

    @PostMapping("/monthly")
    @Operation(
            summary = "Set a monthly budget for the logged-in user",
            description = "Creates a new monthly budget associated with the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Returns the created BudgetResponse object on success.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Monthly budget created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = BudgetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request (e.g., missing or malformed data)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    )
            }
    )
    public ResponseEntity<BudgetResponse> setMonthlyBudget(@RequestBody @Valid BudgetRequest budgetRequest,
                                                           Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.setBudget(userId, budgetRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(budgetResponse);
    }

    @GetMapping("/overrides")
    @Operation(
            summary = "Get all override budgets of the logged-in user",
            description = "Fetches a paginated list of override budgets created by the authenticated user. "
                    + "Requires a valid authentication accessToken. "
                    + "Defaults to page=0 and size=10 if not provided.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Paginated list of override budgets retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BudgetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated"
                    )
            }
    )
    public ResponseEntity<Page<BudgetResponse>> getAllOverrideBudgets(
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
        Page<BudgetResponse> budgetResponses = budgetService.getOverrideBudgets(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponses);
    }

    @GetMapping("/history")
    @Operation(
            summary = "Get user's historical budgets",
            description = "Fetches paginated budget history for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved budget history",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BudgetResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT"),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User not allowed to access this resource")
            }
    )
    public ResponseEntity<Page<BudgetResponse>> getAllHistoryBudgets(
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
        Page<BudgetResponse> budgetResponses = budgetService.getAllHistoryBudgets(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponses);
    }

    @DeleteMapping("/monthly/{month}")
    @Operation(
            summary = "Delete monthly budget",
            description = """
                    Permanently deletes the budget for the specified month for the authenticated user.
                    The `month` path variable must be an ISO-8601 date (`yyyy-MM-dd`).
                    Recommended: use the first day of the month (e.g., `2025-08-01`).
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Budget deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid month format or month out of allowed range"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized – missing or invalid accessToken"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden – user not allowed to delete this budget"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Budget for the given month not found"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict – deletion blocked by business rule (e.g., locked/approved month)"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<Void> deleteMonthlyBudget(@PathVariable
                                                    @Parameter(name = "month",
                                                            description = "Any ISO date within the target month",
                                                            example = "2025-01-01",
                                                            required = true)
                                                    LocalDate month,
                                                    Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        budgetService.deleteBudget(userId, month);
        return ResponseEntity.noContent().build();
    }
}
