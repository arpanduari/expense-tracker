package dev.expensewise.backend.report;

import dev.expensewise.backend.constants.file.FileNameConstants;
import dev.expensewise.backend.exception.ExcelReportGenerationException;
import dev.expensewise.backend.expense.Expense;
import dev.expensewise.backend.user.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author arpan
 * @since 9/10/25
 */
@Component
public class ExcelReportGenerator implements ReportGenerator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public byte[] generate(User user, LocalDate startDate, LocalDate endDate, List<Expense> expenses) {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("ExpenseWise");
            int rowIndex = 0;

            // Styles
            CellStyle headerCellStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // User info section
            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.createCell(0).setCellValue("Email");
            headerRow.createCell(1).setCellValue("Start Date");
            headerRow.createCell(2).setCellValue("End Date");

            Row userRow = sheet.createRow(rowIndex++);
            userRow.createCell(0).setCellValue(user.getEmail());
            userRow.createCell(1).setCellValue(startDate.format(DATE_FORMATTER));
            userRow.createCell(2).setCellValue(endDate.format(DATE_FORMATTER));

            ++rowIndex;

            // Table Headers
            String[] tableHeaders = {"Category", "Description", "Amount(" + user.getCurrency() + ")", "Created Date"};

            Row tableHeader = sheet.createRow(rowIndex++);

            for (int i = 0; i < tableHeaders.length; ++i) {
                Cell cell = tableHeader.createCell(i);
                cell.setCellValue(tableHeaders[i]);
                cell.setCellStyle(headerCellStyle);
            }

            for (Expense expense : expenses) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(expense.getCategory().getName());
                row.createCell(1).setCellValue(expense.getDescription());
                row.createCell(2).setCellValue(expense.getAmount());

                Cell dateCell = row.createCell(3);
                dateCell.setCellValue(Date.valueOf(expense.getCreatedDate()));
                dateCell.setCellStyle(dateStyle);
            }

            for (int i = 0; i < tableHeaders.length; ++i) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception ex) {
            throw new ExcelReportGenerationException("Error while generating excel report.");
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CreationHelper helper = workbook.getCreationHelper();
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(helper.createDataFormat().getFormat("dd-MM-yyyy"));
        return style;
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
