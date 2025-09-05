package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.constants.file.FileNameConstants;
import dev.arpan.expensetracker.report.dto.FileReportResponse;
import dev.arpan.expensetracker.expense.Expense;
import dev.arpan.expensetracker.user.User;
import dev.arpan.expensetracker.exception.ResourceNotFoundException;
import dev.arpan.expensetracker.expense.ExpenseRepository;
import dev.arpan.expensetracker.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author arpan
 * @since 9/4/25
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileReportService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    
    public FileReportResponse generateMonthlyReport(Long userId, LocalDate month) {
        if (month == null) {
            month = LocalDate.now();
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId + ""));
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        List<Expense> expenses = expenseRepository.findByFilters(userId, startDate, endDate, null);
        byte[] report = generateExcelReport(user, month, expenses);
        String fileName = String.format(FileNameConstants.REPORT_FILE_TEMPLATE, user.getUsername(),
                startDate.format(dateTimeFormatter), endDate.format(dateTimeFormatter), "monthly",
                FileNameConstants.REPORT_FILE_EXTENSION_XLSX);
        return FileReportResponse.builder()
                .fileData(report)
                .fileName(fileName)
                .build();
    }


    
    public byte[] generateExcelReport(User user, LocalDate month, List<Expense> expenses) {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            int idx = 0;
            Sheet sheet = workbook.createSheet("ExpenseWise");
            CellStyle headerCellStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerCellStyle.setFont(headerFont);

            // User info section
            Row userRow = sheet.createRow(idx++);
            userRow.createCell(0).setCellValue("Email");
            userRow.createCell(1).setCellValue("Month");
            Row userDataRow = sheet.createRow(idx++);
            userDataRow.createCell(0).setCellValue(user.getEmail());
            userDataRow.createCell(1).setCellValue(month.getMonth().toString());

            ++idx;

            Row tableHeader = sheet.createRow(idx++);
            String[] tableHeaders = {"Category", "Description", "Amount(" + user.getCurrency() + ")", "Created Date"};
            for (int i = 0; i < tableHeaders.length; ++i) {
                Cell cell = tableHeader.createCell(i);
                cell.setCellValue(tableHeaders[i]);
                cell.setCellStyle(headerCellStyle);
            }
            for (Expense expense : expenses) {
                Row row = sheet.createRow(idx++);
                row.createCell(0).setCellValue(expense.getCategory().getName());
                row.createCell(1).setCellValue(expense.getDescription());
                row.createCell(2).setCellValue(expense.getAmount());
                row.createCell(3).setCellValue(expense.getCreatedDate().format(dateTimeFormatter));
            }

            for (int i = 0; i < tableHeaders.length; ++i) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception ex) {
            throw new RuntimeException("Error while generating excel report.");
        }
    }

    
    public byte[] generatePdfReport(LocalDate startDate, LocalDate endDate) {
        return new byte[0];
    }
}
