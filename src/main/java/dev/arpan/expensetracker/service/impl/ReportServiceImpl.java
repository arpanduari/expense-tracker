package dev.arpan.expensetracker.service.impl;

import dev.arpan.expensetracker.dto.*;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.mapper.ReportMapper;
import dev.arpan.expensetracker.projection.IMonthlyReportResponse;
import dev.arpan.expensetracker.repository.ReportRepository;
import dev.arpan.expensetracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author arpan
 * @since 8/20/25
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private ReportRepository reportRepository;

    @Override
    public MonthlyReportResponse getMonthlyReport(Long userId, LocalDate month) {
        IMonthlyReportResponse monthlyReportResponse = reportRepository.findMonthlyReport(userId, month)
                .orElseThrow(() -> new ResourceNotFoundException("Monthly Report", "month", month.toString()));
        return ReportMapper.toMonthlyReportResponse(monthlyReportResponse);
    }

    @Override
    public CategoryWiseMonthlyExpenseResponse getCategoryWiseMonthlyExpense(LocalDate month) {
        return null;
    }

    @Override
    public YearlyReportResponse getYearlyReport(LocalDate year) {
        return null;
    }

    @Override
    public TopExpenseResponse getTopExpense(LocalDate month, int limit) {
        return null;
    }

    @Override
    public InsightResponse getInsight(LocalDate month) {
        return null;
    }
}
