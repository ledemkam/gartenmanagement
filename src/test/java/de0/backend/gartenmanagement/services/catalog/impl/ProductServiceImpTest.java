package de0.backend.gartenmanagement.services.catalog.impl;

import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.exceptions.DuplicateProductException;
import de0.backend.gartenmanagement.exceptions.InvalidPriceException;
import de0.backend.gartenmanagement.exceptions.InsufficientStockException;
import de0.backend.gartenmanagement.exceptions.ProductNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
    @DisplayName("Create should throw when product already exists")
    void create_should_throw_when_product_already_exists() {
        //GIVEN
        doThrow(new DuplicateProductException(requestDto.name()))
                .when(productValidator).checkProductAlreadyExistsByName(requestDto.name());

        //WHEN / THEN
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(DuplicateProductException.class)
                .hasMessage(requestDto.name());

        verify(productValidator).checkProductAlreadyExistsByName(requestDto.name());
        verify(productValidator, never()).validatePrice(requestDto.price());
        verify(productRepository, never()).save(productEntity);
    }

    @Test
    @DisplayName("Create should throw when price is invalid")
    void create_should_throw_when_price_is_invalid() {
        //GIVEN
        doNothing().when(productValidator).checkProductAlreadyExistsByName(requestDto.name());
        doThrow(new InvalidPriceException("Price must be greater than zero"))
                .when(productValidator).validatePrice(requestDto.price());

        //WHEN / THEN
        assertThatThrownBy(() -> service.create(requestDto))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("Price must be greater than zero");

        verify(productValidator).checkProductAlreadyExistsByName(requestDto.name());
        verify(productValidator).validatePrice(requestDto.price());
        verify(productRepository, never()).save(productEntity);
    }

    @Test
    @DisplayName("find product by id  when exists")
    void find_productById_should_return_product_when_exists() {
        //GIVEN
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productMapper.toDto(productEntity)).thenReturn(responseDto);
        //WHEN
        ProductDTOResponse result = service.findById(productEntity.getId());
        //THEN
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("find product by id should throw exception when not found")
    void find_productById_should_return_exception_when_notFound() {
        //GIVEN
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());
        //WHEN/THEN
        assertThatThrownBy(() -> service.findById(productEntity.getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found" );

    }

    @Test
    @DisplayName("update product should return product when exist")
    void update_product_should_return_product_when_exist() {
        //GIVEN
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        doNothing().when(productMapper).updateEntityFromDto(requestDto, productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productMapper.toDto(productEntity)).thenReturn(responseDto);

        //WHEN
        ProductDTOResponse result = service.update(productEntity.getId(), requestDto);

        //THEN
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("name", "rosamarin");
        verify(productRepository).findById(productEntity.getId());
        verify(productMapper).updateEntityFromDto(requestDto, productEntity);
        verify(productRepository).save(productEntity);
        verify(productMapper).toDto(productEntity);
    }

    @Test
    @DisplayName("find all products should return mapped paginated response")
    void find_All_products() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(productEntity), pageable, 1);

        when(productRepository.findByActiveTrue(pageable)).thenReturn(productPage);
        when(productMapper.toDto(productEntity)).thenReturn(responseDto);

        //WHEN
        PageResponse<ProductDTOResponse> result = service.findAll(pageable);

        //THEN
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("page", 0)
                .hasFieldOrPropertyWithValue("size", 10)
                .hasFieldOrPropertyWithValue("totalElements", 1)
                .hasFieldOrPropertyWithValue("totalPages", 1)
                .hasFieldOrPropertyWithValue("hasNext", false)
                .hasFieldOrPropertyWithValue("hasPrevious", false)
                .hasFieldOrPropertyWithValue("isFirst", true)
                .hasFieldOrPropertyWithValue("isLast", true);

        assertThat(result.getContent())
                .isNotNull()
                .hasSize(1)
                .containsExactly(responseDto);

        verify(productRepository).findByActiveTrue(pageable);
        verify(productMapper).toDto(productEntity);
    }

    @Test
    @DisplayName("adjust stock should update product stock when product exists")
    void adjust_Stock_with_succes() {
        //GIVEN
        int quantity = 5;
        Product updatedProduct = Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .category(productEntity.getCategory())
                .price(productEntity.getPrice())
                .stock(productEntity.getStock() + quantity)
                .active(true)
                .build();

        ProductDTOResponse updatedResponse = ProductDTOResponse.builder()
                .id(responseDto.id())
                .name(responseDto.name())
                .category(responseDto.category())
                .price(responseDto.price())
                .stock(responseDto.stock() + quantity)
                .active(true)
                .build();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(updatedResponse);

        //WHEN
        ProductDTOResponse result = service.adjustStock(productEntity.getId(), quantity);

        //THEN
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("stock", 55)
                .hasFieldOrPropertyWithValue("active", true);

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).save(productEntity);
        verify(productMapper).toDto(updatedProduct);
    }

    @Test
    @DisplayName("adjust stock should throw when resulting stock is negative")
    void adjust_stock_should_throw_when_resulting_stock_is_negative() {
        //GIVEN
        int quantity = -60;
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));

        //WHEN / THEN
        assertThatThrownBy(() -> service.adjustStock(productEntity.getId(), quantity))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessage("Insufficient stock for adjustment");

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository, never()).save(productEntity);
        verify(productMapper, never()).toDto(productEntity);
    }

    @Test
    @DisplayName("delete should remove product when product exists")
    void delete_product_should_succes_when_product_exists() {
        //GIVEN
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        doNothing().when(productRepository).delete(productEntity);

        //WHEN
        service.delete(productEntity.getId());

        //THEN
        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).delete(productEntity);
    }

    @Test
    @DisplayName("delete should throw when product does not exist")
    void delete_product_should_throw_when_product_not_found() {
        //GIVEN
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        //WHEN / THEN
        assertThatThrownBy(() -> service.delete(productEntity.getId()))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository, never()).delete(productEntity);
    }

    @Test
    @DisplayName("apply discount should update price when discount is valid")
    void apply_discount_should_succes_when_discount_is_valid() {
        //GIVEN
        BigDecimal discount = new BigDecimal("10");
        Product discountedProduct = Product.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .category(productEntity.getCategory())
                .price(new BigDecimal("9.00"))
                .stock(productEntity.getStock())
                .active(productEntity.getActive())
                .build();

        ProductDTOResponse discountedResponse = ProductDTOResponse.builder()
                .id(responseDto.id())
                .name(responseDto.name())
                .category(responseDto.category())
                .price(new BigDecimal("9.00"))
                .stock(responseDto.stock())
                .active(responseDto.active())
                .build();

        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
        when(productRepository.save(productEntity)).thenReturn(discountedProduct);
        when(productMapper.toDto(discountedProduct)).thenReturn(discountedResponse);

        //WHEN
        ProductDTOResponse result = service.applyDiscount(productEntity.getId(), discount);

        //THEN
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("price", new BigDecimal("9.00"));

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository).save(productEntity);
        verify(productMapper).toDto(discountedProduct);
    }

    @Test
    @DisplayName("apply discount should throw when discount is greater than 50")
    void apply_discount_should_throw_when_discount_is_greater_than_fifty() {
        //GIVEN
        BigDecimal invalidDiscount = new BigDecimal("60");

        //WHEN / THEN
        assertThatThrownBy(() -> service.applyDiscount(productEntity.getId(), invalidDiscount))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("Discount percentage must be between 0 and 50.");

        verify(productRepository, never()).findById(productEntity.getId());
        verify(productRepository, never()).save(productEntity);
    }

    @Test
    @DisplayName("apply discount should throw when product does not exist")
    void apply_discount_should_throw_when_product_not_found() {
        //GIVEN
        BigDecimal discount = new BigDecimal("10");
        when(productRepository.findById(productEntity.getId())).thenReturn(Optional.empty());

        //WHEN / THEN
        assertThatThrownBy(() -> service.applyDiscount(productEntity.getId(), discount))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(productRepository).findById(productEntity.getId());
        verify(productRepository, never()).save(productEntity);
    }
}
