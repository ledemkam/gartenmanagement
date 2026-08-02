package de0.backend.gartenmanagement.controllers.impl;

import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.controllers.IProductController;
import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.services.catalog.impl.ProductServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController implements IProductController {

    private final ProductServiceImp productServiceImp;

    @Override
    public ResponseEntity<ProductDTOResponse> createPost(ProductDTORequest request) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDTOResponse> updateProduct(String id, ProductDTORequest request) {
        return null;
    }

    @Override
    public ResponseEntity<PageResponse<ProductDTOResponse>> getAllProducts(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDTOResponse> getProductById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDTOResponse> getProductByCstegory(ProductCategory category, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDTOResponse> adjustStock(String id, Integer quantity) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String id) {
        return null;
    }

    @Override
    public ResponseEntity<ProductDTOResponse> applyDiscount(String id, BigDecimal discountPercentage) {
        return null;
    }

    @Override
    public ResponseEntity<PageResponse<ProductDTOResponse>> getOutOfStockProducts(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<PageResponse<ProductDTOResponse>> getLowStockProducts(Integer threshold, Pageable pageable) {
        return null;
    }
}
