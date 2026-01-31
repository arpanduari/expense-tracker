package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

/**
 * @author arpan
 * @since 8/5/25
 */
@Schema(description = "Data transfer object for Verification Response")
public record VerifyResponse(
        @Schema(description = "Message", example = "Verification successful")
        String message,

        HttpStatus status) {
}
