package de0.backend.gartenmanagement.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApplicationProperties - Tests unitaires")
class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;

    @BeforeEach
    void setUp() {
        applicationProperties = new ApplicationProperties();
    }

    // ===================== getBusiness =====================

    @Test
    @DisplayName("getBusiness() - doit retourner une instance Business non nulle par défaut")
    void getBusiness() {
        ApplicationProperties.Business business = applicationProperties.getBusiness();

        assertNotNull(business);
        assertEquals("Garten Pro", business.getName());
        assertEquals("kontakt@gartemnt.de", business.getContactEmail());
        assertEquals("0123456789", business.getPhone());
        assertEquals("Gartenstraße 123, D - 75000 MusterStat", business.getAddress());
    }

    // ===================== getStock =====================

    @Test
    @DisplayName("getStock() - doit retourner une instance Stock non nulle par défaut")
    void getStock() {
        ApplicationProperties.Stock stock = applicationProperties.getStock();

        assertNotNull(stock);
        assertEquals(10, stock.getLowStockThreshold());
        assertEquals(7, stock.getReorderDelayDays());
        assertFalse(stock.getAutoReorder());
    }

    // ===================== getPricing =====================

    @Test
    @DisplayName("getPricing() - doit retourner une instance Pricing non nulle par défaut")
    void getPricing() {
        ApplicationProperties.Pricing pricing = applicationProperties.getPricing();

        assertNotNull(pricing);
        assertEquals(new BigDecimal("20.0"), pricing.getDefaultTaxRate());
        assertEquals(new BigDecimal("30.0"), pricing.getMaxDiscountRate());
        assertEquals("EUR", pricing.getCurrency());
    }

    // ===================== setBusiness =====================

    @Test
    @DisplayName("setBusiness() - doit correctement remplacer l'instance Business")
    void setBusiness() {
        ApplicationProperties.Business newBusiness = new ApplicationProperties.Business();
        newBusiness.setName("Nouveau Jardin");
        newBusiness.setContactEmail("info@nouveau-jardin.de");
        newBusiness.setPhone("0987654321");
        newBusiness.setAddress("Neue Straße 1, D - 80000 München");

        applicationProperties.setBusiness(newBusiness);

        ApplicationProperties.Business result = applicationProperties.getBusiness();
        assertSame(newBusiness, result);
        assertEquals("Nouveau Jardin", result.getName());
        assertEquals("info@nouveau-jardin.de", result.getContactEmail());
        assertEquals("0987654321", result.getPhone());
        assertEquals("Neue Straße 1, D - 80000 München", result.getAddress());
    }

    // ===================== setStock =====================

    @Test
    @DisplayName("setStock() - doit correctement remplacer l'instance Stock")
    void setStock() {
        ApplicationProperties.Stock newStock = new ApplicationProperties.Stock();
        newStock.setLowStockThreshold(25);
        newStock.setReorderDelayDays(14);
        newStock.setAutoReorder(true);

        applicationProperties.setStock(newStock);

        ApplicationProperties.Stock result = applicationProperties.getStock();
        assertSame(newStock, result);
        assertEquals(25, result.getLowStockThreshold());
        assertEquals(14, result.getReorderDelayDays());
        assertTrue(result.getAutoReorder());
    }

    // ===================== setPricing =====================

    @Test
    @DisplayName("setPricing() - doit correctement remplacer l'instance Pricing")
    void setPricing() {
        ApplicationProperties.Pricing newPricing = new ApplicationProperties.Pricing();
        newPricing.setDefaultTaxRate(new BigDecimal("19.0"));
        newPricing.setMaxDiscountRate(new BigDecimal("15.0"));
        newPricing.setCurrency("USD");

        applicationProperties.setPricing(newPricing);

        ApplicationProperties.Pricing result = applicationProperties.getPricing();
        assertSame(newPricing, result);
        assertEquals(new BigDecimal("19.0"), result.getDefaultTaxRate());
        assertEquals(new BigDecimal("15.0"), result.getMaxDiscountRate());
        assertEquals("USD", result.getCurrency());
    }

    // ===================== Tests des sous-classes imbriquées =====================

    @Nested
    @DisplayName("Business - Valeurs par défaut et setters")
    class BusinessTests {

        @Test
        @DisplayName("Business - setName() modifie correctement le nom")
        void setName_shouldUpdateName() {
            ApplicationProperties.Business business = new ApplicationProperties.Business();
            business.setName("Test Garten");
            assertEquals("Test Garten", business.getName());
        }

        @Test
        @DisplayName("Business - setContactEmail() modifie correctement l'e-mail")
        void setContactEmail_shouldUpdateEmail() {
            ApplicationProperties.Business business = new ApplicationProperties.Business();
            business.setContactEmail("test@test.de");
            assertEquals("test@test.de", business.getContactEmail());
        }
    }

    @Nested
    @DisplayName("Stock - Valeurs par défaut et setters")
    class StockTests {

        @Test
        @DisplayName("Stock - setLowStockThreshold() modifie correctement le seuil")
        void setLowStockThreshold_shouldUpdateThreshold() {
            ApplicationProperties.Stock stock = new ApplicationProperties.Stock();
            stock.setLowStockThreshold(50);
            assertEquals(50, stock.getLowStockThreshold());
        }

        @Test
        @DisplayName("Stock - setAutoReorder() active correctement la commande automatique")
        void setAutoReorder_shouldToggleAutoReorder() {
            ApplicationProperties.Stock stock = new ApplicationProperties.Stock();
            stock.setAutoReorder(true);
            assertTrue(stock.getAutoReorder());
        }
    }

    @Nested
    @DisplayName("Pricing - Valeurs par défaut et setters")
    class PricingTests {

        @Test
        @DisplayName("Pricing - setDefaultTaxRate() modifie correctement le taux de taxe")
        void setDefaultTaxRate_shouldUpdateTaxRate() {
            ApplicationProperties.Pricing pricing = new ApplicationProperties.Pricing();
            pricing.setDefaultTaxRate(new BigDecimal("7.0"));
            assertEquals(new BigDecimal("7.0"), pricing.getDefaultTaxRate());
        }

        @Test
        @DisplayName("Pricing - setCurrency() modifie correctement la devise")
        void setCurrency_shouldUpdateCurrency() {
            ApplicationProperties.Pricing pricing = new ApplicationProperties.Pricing();
            pricing.setCurrency("GBP");
            assertEquals("GBP", pricing.getCurrency());
        }
    }
}