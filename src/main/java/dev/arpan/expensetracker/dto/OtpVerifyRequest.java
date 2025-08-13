package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/5/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for OTP Verification Request")
public class OtpVerifyRequest {
    @Schema(description = "Verification token")
    private String token;
    @Schema(description = "OTP")
    private String otp;
}
