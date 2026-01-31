package dev.expensewise.backend.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author arpan
 * @since 8/3/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Error Response")
public class ErrorResponse {
    @Schema(description = "Api path", example = "/api/v1/expenses")
    private String apiPath;
    @Schema(description = "Http status code", example = "400")
    private int statusCode;
    @Schema(description = "HTTP Status name", example = "BAD_REQUEST")
    private String status;
    @Schema(description = "Error message", example = "Invalid request")
    private String errorMessage;
    @Schema(description = "Timestamp", example = "2025-08-03T10:15:30")
    private LocalDateTime timestamp;
}
