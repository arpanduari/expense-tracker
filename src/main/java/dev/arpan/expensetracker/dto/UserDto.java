package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/3/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for User")
public class UserDto {
    @Schema(description = "username", example = "johndoe")
    private String username;
    @Schema(description = "email", example = "")
    private String email;
    @Schema(description = "currency", example = "INR")
    private String currency;
}
