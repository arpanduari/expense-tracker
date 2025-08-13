package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data transfer object for Login Request")
public class LoginRequestDTO {
    @Parameter(description = "User identifier", examples = {
            @ExampleObject(name = "Email", value = "johondoe@email.com"),
            @ExampleObject(name = "username", value = "johndoe")
    })
    private String userIdentifier;
    @Schema(description = "User password", example = "verystrongpassword@4321")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
