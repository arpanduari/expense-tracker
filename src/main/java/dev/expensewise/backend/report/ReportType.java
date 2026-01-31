package dev.expensewise.backend.report;

/**
 * @author arpan
 * @since 9/11/25
 */
public enum ReportType {
    PDF, EXCEL;

    public String toKey() {
        return name().toLowerCase();
    }
}
