package de0.backend.gartenmanagement.services.catalog;

import de0.backend.gartenmanagement.exceptions.DuplicateProductException;
import de0.backend.gartenmanagement.exceptions.InvalidPriceException;
import de0.backend.gartenmanagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductValidator {

    private final ProductRepository productRepository;

    public void checkProductAlreadyExistsByName(final String name) {
        productRepository.findByNameIgnoreCase(name)
                .ifPresent(product -> {
                    log.error("Product with name {} already exists", name);
                    throw new DuplicateProductException(name);
                });
    }

    public void validatePrice(final BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("price muss be positiv");
        }
    }
}

