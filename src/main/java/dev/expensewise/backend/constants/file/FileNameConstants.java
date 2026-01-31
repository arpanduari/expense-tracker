package dev.expensewise.backend.constants.file;

/**
 * @author arpan
 * @since 9/4/25
 */
public final class FileNameConstants {
    private FileNameConstants() {
    }

    public static final String REPORT_FILE_TEMPLATE = "expensewise-report-%s-%s-%s-%s.%s";
    public static final String REPORT_FILE_EXTENSION_XLSX = "xlsx";
    public static final String REPORT_FILE_EXTENSION_PDF = "pdf";
    public static final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
