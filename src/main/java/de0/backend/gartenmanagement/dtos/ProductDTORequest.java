package de0.backend.gartenmanagement.dtos;

import de0.backend.gartenmanagement.entities.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDTORequest(
        @NotBlank(message = "{product.name.not-blank}")
        @Size(min = 2, max = 100, message = "{product.name.size}")
        String name,

        @NotNull(message = "{product.category.not-null}")
        ProductCategory category,

        @NotNull(message = "{product.price.not-null}")
        @DecimalMin(value = "0.01", message = "{product.price.min}")
        @Digits(integer = 8, fraction = 2, message = "{product.price.digits}")
        BigDecimal price,

        @NotBlank(message = "{product.description.not-blank}")
        @Size(min = 5, max = 500, message = "{product.description.size}")
        String description,

        @NotNull(message = "{product.stock.not-null}")
        @PositiveOrZero(message = "{product.stock.positive-or-zero}")
        Integer stock
) {
}
