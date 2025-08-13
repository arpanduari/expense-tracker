package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.ExpenseRequestDTO;
import dev.arpan.expensetracker.dto.ExpenseResponseDTO;
import dev.arpan.expensetracker.service.ExpenseService;
import dev.arpan.expensetracker.utils.UserUtil;
import jakarta.validation.Valid;
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
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody @Valid ExpenseRequestDTO expenseRequestDTO,
                                                            Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.addExpense(userId, expenseRequestDTO);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id, Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.getExpenseById(userId, id);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id,
                                                            @RequestBody ExpenseRequestDTO expenseRequestDTO,
                                                            Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        ExpenseResponseDTO expenseResponseDTO = expenseService.updateExpense(userId, id, expenseRequestDTO);
        return ResponseEntity.ok(expenseResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, Authentication authentication) {
        Long userId = UserUtil.getUserId(authentication);
        expenseService.deleteExpense(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponseDTO>> getExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            Authentication authentication
    ) {
        Long userId = UserUtil.getUserId(authentication);
        Page<ExpenseResponseDTO> expenseResponses = expenseService.getExpenses(userId, page, size, sortBy, direction,
                startDate, endDate, categoryId);
        return ResponseEntity.ok(expenseResponses);
    }
}
