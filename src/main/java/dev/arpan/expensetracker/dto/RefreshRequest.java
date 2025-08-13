package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/4/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Refresh Request")
public class RefreshRequest {
    @Schema(description = "Refresh token")
    private String refreshToken;
}
