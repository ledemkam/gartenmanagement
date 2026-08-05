package de0.backend.gartenmanagement.config;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Business business = new Business();
    private Stock stock = new Stock();
    private Pricing pricing = new Pricing();

    @Getter
    @Setter
    public static class Business {
        @NotBlank(message = "The company name cannot be empty")
        private String name = "Garten Pro";

        @Email(message = "Invalid email")
        private String contactEmail = "kontakt@gartemnt.de";

        @Pattern(regexp = "\\d{10}", message = "The phone number must contain 10 digits")
        private String phone = "0123456789";

        private String address = "Gartenstraße 123, D - 75000 MusterStat";
    }

    @Getter
    @Setter
    public static class Stock {
        @Min(value = 1, message = "The minimum threshold must be at least 1")
        @Max(value = 100, message = "The maximum threshold cannot exceed 100")
        private Integer lowStockThreshold = 10;

        @Min(value = 1, message = "The reorder delay must be at least 1 day")
        private Integer reorderDelayDays = 7;

        private Boolean autoReorder = false;
    }

    @Getter
    @Setter
    public static class Pricing {
        @DecimalMin(value = "0.0", message = "VAT cannot be negative")
        @DecimalMax(value = "100.0", message = "VAT cannot exceed 100%")
        private BigDecimal defaultTaxRate = new BigDecimal("20.0");

        @DecimalMin(value = "0.0", message = "The maximum discount cannot be negative")
        @DecimalMax(value = "50.0", message = "The maximum discount cannot exceed 50%")
        private BigDecimal maxDiscountRate = new BigDecimal("30.0");

        private String currency = "EUR";
    }
}
