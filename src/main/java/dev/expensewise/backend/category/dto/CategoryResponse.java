package dev.expensewise.backend.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author arpan
 * @since 8/2/25
 */
@Schema(description = "Data transfer object for Category Response")
public record CategoryResponse(
        @Schema(description = "Category id", example = "1")
        Long id,
        @Schema(description = "Category name", example = "FOOD")
        String name,
        @Schema(description = "Category icon", example = "\uD83C\uDF72")
        String icon) {
}
