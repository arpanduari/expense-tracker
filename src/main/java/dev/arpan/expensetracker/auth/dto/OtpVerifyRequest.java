package dev.arpan.expensetracker.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/5/25
 */
@Schema(description = "Data transfer object for OTP Verification Request")
public record OtpVerifyRequest(
        @Schema(description = "Verification token")
        String token,
        @Schema(description = "OTP")
        String otp) {
}
