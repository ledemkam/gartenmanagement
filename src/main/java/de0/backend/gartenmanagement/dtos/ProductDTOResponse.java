package de0.backend.gartenmanagement.dtos;

import de0.backend.gartenmanagement.entities.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductDTOResponse(
        @NotBlank(message = "Die ID darf nicht leer sein")
        String id,

        @NotBlank(message = "Der Produktname darf nicht leer sein")
        @Size(min = 2, max = 100, message = "Der Name muss zwischen 2 und 100 Zeichen lang sein")
        String name,

        @NotNull(message = "Die Kategorie ist erforderlich")
        ProductCategory category,

        @NotNull(message = "Der Preis ist erforderlich")
        @DecimalMin(value = "0.01", message = "Der Preis muss größer als 0 sein")
        @Digits(integer = 8, fraction = 2, message = "Der Preis darf maximal 8 ganze Ziffern und 2 Dezimalstellen haben")
        BigDecimal price,

        @NotBlank(message = "Die Beschreibung darf nicht leer sein")
        @Size(min = 5, max = 500, message = "Die Beschreibung muss zwischen 5 und 500 Zeichen lang sein")
        String description,

        @NotNull(message = "Der Status 'aktiv' ist erforderlich")
        Boolean active,

        @NotNull(message = "Der Lagerbestand ist erforderlich")
        @PositiveOrZero(message = "Der Lagerbestand kann nicht negativ sein")
        Integer stock,

        @NotNull(message = "Das Erstellungsdatum ist erforderlich")
        LocalDateTime creationDate
) {
}
