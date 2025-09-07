package dev.arpan.expensetracker.report.dto;

/**
 * @author arpan
 * @since 9/4/25
 */
public record FileReportResponse(
        String fileName,
        byte[] fileData) {
}
