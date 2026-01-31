package dev.expensewise.backend.exception;

/**
 * @author arpan
 * @since 12/28/25
 */
public class ExcelReportGenerationException extends RuntimeException {
    public ExcelReportGenerationException(String message) {
        super(message);
    }
}
