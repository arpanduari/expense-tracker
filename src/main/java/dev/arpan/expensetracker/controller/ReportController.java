package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.constants.FileNameConstants;
import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.service.FileReportService;
import dev.arpan.expensetracker.service.ReportService;
import dev.arpan.expensetracker.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final FileReportService fileReportService;
    private final UserUtil userUtil;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        MonthlyReportResponse monthlyReportResponse = reportService.getMonthlyReport(userId, month);
        return ResponseEntity.ok(monthlyReportResponse);
    }

    @GetMapping("/category-wise")
    public ResponseEntity<CategoryWiseMonthlyExpenseResponse> getMonthlyReportByCategory(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        CategoryWiseMonthlyExpenseResponse response = reportService.getCategoryWiseMonthlyExpense(userId, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @RequestParam(required = false) Integer year,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        YearlyReportResponse response = reportService.getYearlyReport(userId, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-expenses")
    public ResponseEntity<TopExpenseResponse> getTopExpenses(
            @RequestParam(required = false) LocalDate month,
            @RequestParam(required = false, defaultValue = "5") int size,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        TopExpenseResponse response = reportService.getTopExpense(userId, month, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> getInsights(
            @RequestParam(required = false) LocalDate month,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        InsightResponse insightResponse = reportService.getInsight(userId, month);
        return ResponseEntity.ok(insightResponse);
    }

    @GetMapping("/excel/monthly")
    public ResponseEntity<byte[]> getExcelReport(@RequestParam(required = false) LocalDate month, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        FileReportResponse fileReportResponse = fileReportService.generateMonthlyReport(userId, month);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileReportResponse.getFileName())
                .contentType(MediaType.parseMediaType(FileNameConstants.EXCEL_MEDIA_TYPE))
                .body(fileReportResponse.getFileData());
    }
}
