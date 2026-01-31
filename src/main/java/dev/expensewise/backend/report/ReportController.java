package dev.expensewise.backend.report;

import dev.expensewise.backend.report.dto.*;
import dev.expensewise.backend.user.util.UserUtil;
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
import java.util.List;
import java.util.Map;

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

    @GetMapping("/monthly/export")
    public ResponseEntity<byte[]> getMonthlyReport(@RequestParam(required = false) LocalDate month, @RequestParam ReportType type, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        FileReportResponse fileReportResponse = fileReportService.generateMonthlyReport(userId, month, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileReportResponse.fileName())
                .contentType(MediaType.parseMediaType(fileReportResponse.mediaType()))
                .body(fileReportResponse.fileData());
    }

    @GetMapping("/yearly/export")
    public ResponseEntity<byte[]> getYearlyReport(@RequestParam(required = false) Integer year, @RequestParam ReportType type, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        FileReportResponse fileReportResponse = fileReportService.generateYearlyReport(userId, year, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileReportResponse.fileName())
                .contentType(MediaType.parseMediaType(fileReportResponse.mediaType()))
                .body(fileReportResponse.fileData());
    }

    @GetMapping("/custom/export")
    public ResponseEntity<byte[]> getYearlyReport(@RequestParam LocalDate startDate,
                                                  @RequestParam LocalDate endDate,
                                                  @RequestParam ReportType type, Authentication authentication) {
        Long userId = userUtil.getUserId(authentication);
        FileReportResponse fileReportResponse = fileReportService.generateCustomReport(userId, startDate, endDate, type);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileReportResponse.fileName())
                .contentType(MediaType.parseMediaType(fileReportResponse.mediaType()))
                .body(fileReportResponse.fileData());
    }

    @GetMapping("/daily-expenses")
    public ResponseEntity<Map<LocalDate, List<DailyExpenseResponse>>> getDailyExpenseByDate(
            @RequestParam LocalDate month,
            Authentication authentication
    ) {
        Long userId = userUtil.getUserId(authentication);
        Map<LocalDate, List<DailyExpenseResponse>> dailyExpenses = reportService.getExpensesGroupByDate(userId, month);
        return ResponseEntity.ok(dailyExpenses);
    }
}
