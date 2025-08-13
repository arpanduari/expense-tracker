package dev.arpan.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author arpan
 * @since 8/2/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data transfer object for Category Response")
public class CategoryResponse {
    @Schema(description = "Category id", example = "1")
    private Long id;
    @Schema(description = "Category name", example = "FOOD")
    private String name;
}
