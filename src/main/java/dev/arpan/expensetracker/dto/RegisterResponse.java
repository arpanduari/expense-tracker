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
@Schema(description = "Data transfer object for Register Response")
public class RegisterResponse {
    @Schema(description = "Message", example = "Registration successful")
    private String message;
    @Schema(description = "Verification URL")
    private String verificationUrl;
}
