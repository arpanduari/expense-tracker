package dev.arpan.expensetracker.dto;

import org.springframework.http.HttpStatus;

/**
 * @author arpan
 * @since 8/5/25
 */

public record VerifyResponse(String message, HttpStatus status) {
}
