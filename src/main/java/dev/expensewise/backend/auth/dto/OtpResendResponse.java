package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/5/25
 */
@Schema(description = "Data transfer object for OTP Resend Response")
public record OtpResendResponse(
        @Schema(description = "Message", example = "OTP resent successfully")
        String message,
        @Schema(description = "Verification URL")
        String verificationUrl) {
}
