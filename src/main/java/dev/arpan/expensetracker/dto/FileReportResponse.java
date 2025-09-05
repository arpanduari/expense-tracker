package dev.arpan.expensetracker.dto;

import lombok.*;

/**
 * @author arpan
 * @since 9/4/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileReportResponse {
    private String fileName;
    private byte[] fileData;
}
