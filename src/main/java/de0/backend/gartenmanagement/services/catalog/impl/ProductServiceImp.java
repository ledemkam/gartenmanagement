package de0.backend.gartenmanagement.services.catalog.impl;

import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.exceptions.InsufficientStockException;
import de0.backend.gartenmanagement.exceptions.InvalidPriceException;
import de0.backend.gartenmanagement.exceptions.ProductNotFoundException;
import de0.backend.gartenmanagement.mapper.ProductMapper;
import de0.backend.gartenmanagement.repository.ProductRepository;
import de0.backend.gartenmanagement.services.catalog.ProductService;
import de0.backend.gartenmanagement.services.catalog.ProductValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidator productValidator;
    private static final Integer LOW_STOCK_THRESHOLD = 10;
    private static final BigDecimal MAX_DISCOUNT = new BigDecimal("50");

    @Override
    public void create(final ProductDTORequest request) {
        log.info("Create new product with name: {}", request.name());
        productValidator.checkProductAlreadyExistsByName(request.name());
        productValidator.validatePrice(request.price());
        final Product entity = productMapper.toEntityFromCreate(request);
        log.info("Saving product: {}", entity);
        productRepository.save(entity);
    }

    @Override
    public ProductDTOResponse findById(final String id){
        log.debug("Fetching product with id: {}", id);
        Product product = findProductOrThrow(id);
        return productMapper.toDto(product);
    }

    @Override
    public void update(final String id, final ProductDTORequest request) {
        log.info("Updating product with id: {}", id);
        Product existingProduct = findProductOrThrow(id);
        productValidator.validatePrice(request.price());
         productMapper.updateEntityFromDto(request, existingProduct);
         Product updaptingProduct = productRepository.save(existingProduct);

         log.info("Product with id {} updated successfully: {}", id, updaptingProduct);
         productMapper.toDto(updaptingProduct);
    }

    @Override
    public PageResponse<ProductDTOResponse> findAll(final Pageable pageable) {
        log.debug("Fetching products with paging: page={}, size={}",
                pageable.getPageNumber(),
                pageable.getPageSize());
        return PageResponse.of(productRepository.findByActiveTrue(pageable)
                .map(productMapper::toDto));
    }

    @Override
    public PageResponse<ProductDTOResponse> findByCategory(final ProductCategory category,
                                                           final Pageable pageable) {
        log.debug("Fetching products by category {} with paging: page={}, size={}",
                category, pageable.getPageNumber(), pageable.getPageSize());
        return PageResponse.of(productRepository.findByCategoryAndActiveTrue(category, pageable)
                .map(productMapper::toDto));
    }

    @Override
    public void delete(final String id) {
        log.info("Deleting product with id: {}", id);
        Product existingProduct = findProductOrThrow(id);
        productRepository.delete(existingProduct);
        log.info("Product with id {} deleted successfully", id);
    }

    @Override
    public ProductDTOResponse adjustStock(final String id,
                                          final Integer quantity) {
        log.info("Adjusting stock for product with id: {}, quantity: {}", id, quantity);
        Product product = findProductOrThrow(id);

        final int newStock = product.getStock() + quantity;
        if (newStock < 0) {
            log.error("Insufficient stock for product with id {}", id);
            throw new InsufficientStockException("Insufficient stock for adjustment");
        }

        product.setStock(newStock);

        if (newStock > 0 && newStock <= LOW_STOCK_THRESHOLD) {
             log.warn("ALERT: Low stock for product {}: new stock = {}",
             product.getName(), newStock);
             }
        //deactivated when insufficient stocks
        if (newStock == 0){
            log.warn("ALERT: Product {} is out of stock and will be deactivated",
                    product.getName());
            product.setActive(false);
        }
        final Product updatedProduct = productRepository.save(product);
        log.info("Stock adjusted successfully for product with id {}: new stock = {}",
                id, newStock);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public ProductDTOResponse applyDiscount(final String id,
                                            final BigDecimal discountPercentage) {
        log.info("Applying discount for product with id: {}, discount percentage: {}", id, discountPercentage);

        //validate discount percentage betwen 0 and 50%
        if (discountPercentage.compareTo(BigDecimal.ZERO) < 0 ||
                discountPercentage.compareTo(MAX_DISCOUNT) > 0) {
            log.error("Invalid discount percentage: {}. Must be between 0 and 50.", discountPercentage);
            throw new InvalidPriceException("Discount percentage must be between 0 and 50.");
        }
        Product product = findProductOrThrow(id);

        BigDecimal originalPrice = product.getPrice();
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                discountPercentage.divide(new BigDecimal("100"), 4,
                        RoundingMode.HALF_UP)
        );
        BigDecimal newPrice = originalPrice.multiply(discountMultiplier)
                .setScale(2, RoundingMode.
                        HALF_UP);
        log.info("originalPrice: {}- New price: {}", originalPrice,
                newPrice);
        product.setPrice(newPrice);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public PageResponse<ProductDTOResponse> getOutOfStockProducts(final Pageable pageable) {
        log.debug("searching for out of stock products");
        return PageResponse.of(productRepository.findLowStockProducts(0, pageable)
                .map(productMapper::toDto));
    }

    @Override
    public PageResponse<ProductDTOResponse> getLowStockProducts(final Integer threshold,
                                                                final Pageable pageable) {
        log.debug("searching with stock products");
        return PageResponse.of(productRepository.findLowStockProducts(threshold, pageable)
                .map(productMapper::toDto));
    }

    private Product findProductOrThrow(final String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ProductNotFoundException("Product not found");
                });
    }

}
