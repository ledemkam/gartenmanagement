package de0.backend.gartenmanagement.controllers.impl;

import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.controllers.IProductController;
import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.services.catalog.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController implements IProductController {

    private final ProductService productService;


    @Override
    @PostMapping
    public ResponseEntity<ProductDTOResponse> createPost(
            @Valid
            @RequestBody
            final ProductDTORequest request) {
        log.info("Received request to create product : {}", request);
        ProductDTOResponse createProduct = productService.create(request);
        URI location = URI.create("/api/v1/products/"+ createProduct.id());
        return ResponseEntity.created(location).body(createProduct);

    }

    @Override
    @GetMapping(path = "/{product-id}")
    public ResponseEntity<ProductDTOResponse> getProductById(
           @PathVariable("product-id")
           final String id) {
        log.debug("Received request to get product by id: {}", id);
        ProductDTOResponse product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @Override
    @PutMapping(path = "/{product-id}")
    public ResponseEntity<ProductDTOResponse> updateProduct(
                    @NotNull(message = "{product.id.not-null}")
                    @PathVariable("product-id")
                    final String id,
                    @Valid
                    @RequestBody
                    final ProductDTORequest request) {
        log.info("Received request to update product with id {}: {}", id, request);
        ProductDTOResponse updatedProduct = productService.update(id, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedProduct);
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<ProductDTOResponse>> getAllProducts(Pageable pageable) {
        log.debug("Received request to get all products, page: {}", pageable);
        PageResponse<ProductDTOResponse> products = productService.findAll(pageable);
        return ResponseEntity.ok(products);
    }

    @Override
    @GetMapping(path = "/category/{category}")
    public ResponseEntity<PageResponse<ProductDTOResponse>> getProductByCategory(
            @PathVariable
            final ProductCategory category,
            final Pageable pageable) {
        log.debug("Received request to get products by category: {}", category);
        PageResponse<ProductDTOResponse> products = productService.findByCategory(category, pageable);
        return ResponseEntity.ok(products);
    }

    @Override
    @PatchMapping(path = "/{product-id}/stock")
    public ResponseEntity<ProductDTOResponse> adjustStock(
            @PathVariable("product-id")
            final String id,
            @RequestParam final Integer quantity) {
        log.info("Received request to adjust stock for product {} by quantity {}", id, quantity);
        ProductDTOResponse updatedProduct = productService.adjustStock(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    @DeleteMapping(path = "/{product-id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("product-id")
            final String id) {
        log.info("Received request to delete product with id: {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{product-id}/discount")
    public ResponseEntity<ProductDTOResponse> applyDiscount(
            @PathVariable("product-id")
            final String id,
            @RequestParam final BigDecimal discountPercentage) {
        log.info("Received request to apply discount for product {} with percentage {}", id, discountPercentage);
        ProductDTOResponse updatedProduct = productService.applyDiscount(id, discountPercentage);
        return ResponseEntity.ok(updatedProduct);
    }

    @Override
    @GetMapping(path = "/search/out-of-stock")
    public ResponseEntity<PageResponse<ProductDTOResponse>> getOutOfStockProducts(
            final Pageable pageable) {
        log.info("Received request to get out-of-stock products, page: {}", pageable);
        PageResponse<ProductDTOResponse> products = productService.getOutOfStockProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Override
    @GetMapping(path = "/search/low-stock")
    public ResponseEntity<PageResponse<ProductDTOResponse>> getLowStockProducts(
            @RequestParam(defaultValue = "10")
            final Integer threshold,
            final Pageable pageable) {
        log.info("Received request to get low-stock products with threshold {}, page: {}", threshold, pageable);
        PageResponse<ProductDTOResponse> products = productService.getLowStockProducts(threshold, pageable);
        return ResponseEntity.ok(products);
    }
}
