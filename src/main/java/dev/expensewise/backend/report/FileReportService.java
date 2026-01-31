package dev.expensewise.backend.report;

import dev.expensewise.backend.constants.file.FileNameConstants;
import dev.expensewise.backend.exception.ResourceNotFoundException;
import dev.expensewise.backend.expense.Expense;
import dev.expensewise.backend.expense.ExpenseRepository;
import dev.expensewise.backend.report.dto.FileReportResponse;
import dev.expensewise.backend.user.User;
import dev.expensewise.backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author arpan
 * @since 9/4/25
 */
@Service
public class FileReportService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final Map<ReportType, ReportGenerator> reportGeneratorMap;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FileReportService(ExpenseRepository expenseRepository, UserRepository userRepository,
                             List<ReportGenerator> generators) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.reportGeneratorMap = generators.stream()
                .collect(Collectors.toMap(ReportGenerator::getType, g -> g));
    }

    public FileReportResponse generateMonthlyReport(Long userId, LocalDate month, ReportType type) {
        LocalDate baseMonth = Optional.ofNullable(month).orElse(LocalDate.now());
        LocalDate startDate = baseMonth.withDayOfMonth(1);
        LocalDate endDate = baseMonth.withDayOfMonth(baseMonth.lengthOfMonth());
        return generateReport(userId, startDate, endDate, "monthly", type);
    }

    public FileReportResponse generateYearlyReport(Long userId, Integer year, ReportType type) {
        Integer targetYear = Optional.ofNullable(year).orElse(LocalDate.now().getYear());
        LocalDate startDate = LocalDate.of(targetYear, 1, 1);
        LocalDate endDate = LocalDate.of(targetYear, 12, 31);
        return generateReport(userId, startDate, endDate, "yearly", type);
    }

    public FileReportResponse generateCustomReport(Long userId, LocalDate startDate, LocalDate endDate, ReportType type) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return generateReport(userId, startDate, endDate, "custom", type);
    }

    public FileReportResponse generateReport(Long userId, LocalDate startDate, LocalDate endDate, String timeType, ReportType type) {
        User user = findUserOrThrow(userId);
        List<Expense> expenses = expenseRepository.findByFilters(userId, startDate, endDate, null);
        return generateFile(user, startDate, endDate, expenses, timeType, type);
    }

    public FileReportResponse generateFile(User user, LocalDate startDate, LocalDate endDate, List<Expense> expenses, String timeType, ReportType type) {
        ReportGenerator generator = reportGeneratorMap.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("Invalid report type" + type.toKey());
        }
        byte[] fileData = generator.generate(user, startDate, endDate, expenses);
        String fileName = String.format(FileNameConstants.REPORT_FILE_TEMPLATE, user.getUsername(),
                startDate.format(dateTimeFormatter),
                endDate.format(dateTimeFormatter),
                timeType,
                generator.getExtension());
        return new FileReportResponse(fileName, fileData, generator.getMediaType());
    }

    public User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));
    }
}
