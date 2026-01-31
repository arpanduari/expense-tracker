package dev.expensewise.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * @author arpan
 * @since 8/2/25
 */
@Schema(description = "Data transfer object for Register Request")
public record RegisterRequest(
        @Schema(description = "User name", example = "johndoe")
        @Length(min = 4, max = 15, message = "Username must be between 4 and 15 characters")
        String username,
        @Schema(description = "User email", example = "")
        @Length(min = 5, max = 50, message = "Email must be between 4 and 50 characters")
        @Email(message = "Invalid email address")
        String email,
        @Schema(description = "User password", example = "")
        @Length(min = 8, message = "Password must be at least 8 characters long")
        String password,
        @Length(min = 3, max = 3, message = "Currency must be 3 characters long")
        String currency) {
}
