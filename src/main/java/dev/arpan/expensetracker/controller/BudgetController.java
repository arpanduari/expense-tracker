package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.BudgetRequest;
import dev.arpan.expensetracker.dto.BudgetResponse;
import dev.arpan.expensetracker.service.BudgetService;
import dev.arpan.expensetracker.utils.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping
    @Operation(summary = "Get budget for the current month or Get the default budget",
            description = "Fetches a user BudgetResponse Object from the database using the provided month",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BudgetResponse object for the provided month",
                            content = @Content(schema = @Schema(implementation = BudgetResponse.class))
                    )
            }
    )
    public ResponseEntity<BudgetResponse> getBudget(@RequestParam(required = false, value = "month")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                    LocalDate month,
                                                    Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.getBudget(userId, month);
        return ResponseEntity.ok(budgetResponse);
    }

    @PostMapping("/default")
    public ResponseEntity<BudgetResponse> setDefaultBudget(@RequestBody BudgetRequest budgetRequest,
                                                           Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.setDefaultBudget(userId, budgetRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(budgetResponse);
    }

    @GetMapping("/default")
    public ResponseEntity<BudgetResponse> getDefaultBudget(Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        BudgetResponse defaultBudget = budgetService.getDefaultBudget(userId);
        return ResponseEntity.ok(defaultBudget);
    }

    @PostMapping("/monthly")
    public ResponseEntity<BudgetResponse> setMonthlyBudget(@RequestBody BudgetRequest budgetRequest,
                                                           Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        BudgetResponse budgetResponse = budgetService.setBudget(userId, budgetRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(budgetResponse);
    }

    @GetMapping("/overrides")
    public ResponseEntity<Page<BudgetResponse>> getAllOverrideBudgets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Long userId = UserUtil.getUserId(authentication);
        Page<BudgetResponse> budgetResponses = budgetService.getOverrideBudgets(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponses);
    }

    @GetMapping("/history")
    public ResponseEntity<Page<BudgetResponse>> getAllHistoryBudgets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Long userId = UserUtil.getUserId(authentication);
        Page<BudgetResponse> budgetResponses = budgetService.getAllHistoryBudgets(userId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(budgetResponses);
    }

    @DeleteMapping("/monthly/{month}")
    public ResponseEntity<Void> deleteMonthlyBudget(@PathVariable LocalDate month, Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        budgetService.deleteBudget(userId, month);
        return ResponseEntity.noContent().build();
    }
}
