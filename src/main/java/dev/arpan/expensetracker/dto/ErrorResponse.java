package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

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
    private HttpStatus status;
    @Schema(description = "Error message", example = "Invalid request")
    private String errorMessage;
    @Schema(description = "Timestamp", example = "2025-08-03T10:15:30")
    private LocalDateTime timestamp;
}
