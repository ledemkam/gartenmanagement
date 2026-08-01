package de0.backend.gartenmanagement.services.catalog.impl;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.mapper.ProductMapper;
import de0.backend.gartenmanagement.repository.ProductRepository;
import de0.backend.gartenmanagement.services.catalog.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("unit test for ProductServiceImp")
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductValidator productValidator;


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
        //GIVEN
        doNothing().when(productValidator).checkProductAlreadyExistsByName(requestDto.name());
        doNothing().when(productValidator).validatePrice(requestDto.price());
        when(productMapper.toEntityFromCreate(requestDto)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toDto(productEntity)).thenReturn(responseDto);

        //WHEN
        ProductDTOResponse result = service.create(requestDto);

        //THEN
        assertThat(result).isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("name", "rosamarin");
        verify(productValidator).checkProductAlreadyExistsByName(requestDto.name());
        verify(productValidator).validatePrice(requestDto.price());
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