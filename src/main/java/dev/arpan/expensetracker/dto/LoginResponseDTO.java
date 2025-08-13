package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Login Response")
public class LoginResponseDTO {
    @Schema(description = "JWT token")
    private String token;
    @Schema(description = "Refresh JWT token")
    private String refreshToken;
    @Schema(description = "User name")
    private String username;
}
