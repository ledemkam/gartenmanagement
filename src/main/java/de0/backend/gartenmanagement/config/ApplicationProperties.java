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
        @NotBlank(message = "{app.business.name.not-blank}")
        private String name = "Garten Pro";

        @Email(message = "{app.business.contact-email.invalid}")
        private String contactEmail = "kontakt@gartemnt.de";

        @Pattern(regexp = "\\d{10}", message = "{app.business.phone.pattern}")
        private String phone = "0123456789";

        private String address = "Gartenstraße 123, D - 75000 MusterStat";
    }

    @Getter
    @Setter
    public static class Stock {
        @Min(value = 1, message = "{app.stock.low-threshold.min}")
        @Max(value = 100, message = "{app.stock.low-threshold.max}")
        private Integer lowStockThreshold = 10;

        @Min(value = 1, message = "{app.stock.reorder-delay.min}")
        private Integer reorderDelayDays = 7;

        private Boolean autoReorder = false;
    }

    @Getter
    @Setter
    public static class Pricing {
        @DecimalMin(value = "0.0", message = "{app.pricing.default-tax.min}")
        @DecimalMax(value = "100.0", message = "{app.pricing.default-tax.max}")
        private BigDecimal defaultTaxRate = new BigDecimal("20.0");

        @DecimalMin(value = "0.0", message = "{app.pricing.max-discount.min}")
        @DecimalMax(value = "50.0", message = "{app.pricing.max-discount.max}")
        private BigDecimal maxDiscountRate = new BigDecimal("30.0");

        private String currency = "EUR";
    }
}
