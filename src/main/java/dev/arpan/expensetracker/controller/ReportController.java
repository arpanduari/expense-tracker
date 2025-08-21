package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.service.ReportService;
import dev.arpan.expensetracker.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/20/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        Long userId = UserUtil.getUserId(authentication);
        MonthlyReportResponse monthlyReportResponse = reportService.getMonthlyReport(userId, month);
        return ResponseEntity.ok(monthlyReportResponse);
    }

    @GetMapping("/category-wise")
    public ResponseEntity<CategoryWiseMonthlyExpenseResponse> getMonthlyReportByCategory(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        return null;
    }

    @GetMapping("/yearly")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @RequestParam(required = false) LocalDate year,
            Authentication authentication
    ) {
        return null;
    }

    @GetMapping("/top-expenses")
    public ResponseEntity<TopExpenseResponse> getTopExpenses(
            @RequestParam(required = false) LocalDate month,
            @RequestParam(required = false, defaultValue = "5") int limit,
            Authentication authentication
    ) {
        return null;
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> getInsights(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        return null;
    }
}
