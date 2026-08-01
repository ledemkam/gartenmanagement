package de0.backend.gartenmanagement.services.catalog.impl;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.mapper.ProductMapper;
import de0.backend.gartenmanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("unit test for ProductServiceImp")
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;


    @InjectMocks
    private ProductServiceImp service;

    private Product productEntity;
    private ProductDTOResponse responseDto;
    private ProductDTORequest requestDto;


    @BeforeEach
    void setUp() {
        productEntity = Product.builder()
                .id("1")
                .name("rosamarin")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("10.00"))
                .stock(50)
                .active(true)
                .build();

        requestDto = ProductDTORequest.builder()
                .name("rosamarin")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("10.00"))
                .stock(50)
                .build();

        responseDto = ProductDTOResponse.builder()
                .id("1")
                .name("rosamarin")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("10.00"))
                .stock(50)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Create should product when not exist")
    void create_should_product_when_not_exist() {
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByCategory() {
    }

    @Test
    void delete() {
    }

    @Test
    void adjustStock() {
    }

    @Test
    void applyDiscount() {
    }
}