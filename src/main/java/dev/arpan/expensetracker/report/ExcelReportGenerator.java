package dev.arpan.expensetracker.report;

import dev.arpan.expensetracker.constants.file.FileNameConstants;
import dev.arpan.expensetracker.expense.Expense;
import dev.arpan.expensetracker.user.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author arpan
 * @since 9/10/25
 */
@Component
public class ExcelReportGenerator implements ReportGenerator {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public byte[] generate(User user, LocalDate startDate, LocalDate endDate, List<Expense> expenses) {
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
            userRow.createCell(1).setCellValue("Start Date");
            Row userDataRow = sheet.createRow(idx++);
            userDataRow.createCell(0).setCellValue(user.getEmail());
            userDataRow.createCell(1).setCellValue(startDate.format(dateTimeFormatter));
            userDataRow.createCell(2).setCellValue(endDate.format(dateTimeFormatter));

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

    @Override
    public String getExtension() {
        return FileNameConstants.REPORT_FILE_EXTENSION_XLSX;
    }

    @Override
    public String getMediaType() {
        return FileNameConstants.EXCEL_MEDIA_TYPE;
    }

    @Override
    public ReportType getType() {
        return ReportType.EXCEL;
    }
}
