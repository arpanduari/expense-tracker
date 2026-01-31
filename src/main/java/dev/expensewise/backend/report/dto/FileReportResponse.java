package dev.expensewise.backend.report.dto;

/**
 * @author arpan
 * @since 9/4/25
 */
public record FileReportResponse(
        String fileName,
        byte[] fileData,
        String mediaType
) {
}
