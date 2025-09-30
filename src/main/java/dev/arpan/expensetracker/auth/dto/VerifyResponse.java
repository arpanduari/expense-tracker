package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

/**
 * @author arpan
 * @since 8/5/25
 */
@Schema(description = "")
public record VerifyResponse(String message, HttpStatus status) {
}
