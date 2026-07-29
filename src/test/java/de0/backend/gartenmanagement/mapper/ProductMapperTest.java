package de0.backend.gartenmanagement.mapper;

import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("ProductMapperTest")
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    @DisplayName("should convert entity to dto")
    void toDto() {
        // Given
        Product product = Product.builder()
                .id(String.valueOf(1L))
                .name("Rosebush")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("15.99"))
                .stock(50)
                .description("Beau rosier rouge")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        // When
        ProductDTOResponse dto = productMapper.toDto(product);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo("1");
        assertThat(dto.name()).isEqualTo("Rosebush");
        assertThat(dto.category()).isEqualTo(ProductCategory.PLANT);
        assertThat(dto.price()).isEqualByComparingTo(new BigDecimal("15.99"));
        assertThat(dto.stock()).isEqualTo(50);
        assertThat(dto.active()).isTrue();
    }

    @Test
    @DisplayName("should convert dto to entity")
    void toEntity() {
        // Given
        ProductDTOResponse productdto = ProductDTOResponse.builder()
                .id(String.valueOf(1L))
                .name("Mower")
                .category(ProductCategory.TOOL)
                .price(new BigDecimal("205.99"))
                .stock(10)
                .description("lectric lawn mower")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        // When
        Product entity = productMapper.toEntity(productdto);

        // Then
        assertThat(entity).isNotNull();
    }

    @Test
    @DisplayName("should convert create dto to entity")
    void toEntityFromCreate() {
        // Given
        ProductDTORequest createDto = ProductDTORequest.builder()
                .name("organicbio")
                .category(ProductCategory.FERTILIZER)
                .price(new BigDecimal("12.50"))
                .stock(100)
                .description("fertilizer bio")
                .build();

        // When
        Product entity = productMapper.toEntityFromCreate(createDto);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("organicbio");
        assertThat(entity.getActive()).isTrue(); // default value
    }

    @Test
    @DisplayName("should update entity from dto")
    void updateEntityFromDto() {
        // Given
        Product existingProduct = Product.builder()
                .id("1")
                .name("alte name")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("10.00"))
                .stock(5)
                .active(false)
                .creationDate(LocalDateTime.now().minusDays(10))
                .build();

        ProductDTOResponse updateDto = ProductDTOResponse.builder()
                .id("1")
                .name("Neu name")
                .category(ProductCategory.TOOL)
                .price(new BigDecimal("25.00"))
                .stock(50)
                .description("name update")
                .active(true)
                .creationDate(existingProduct.getCreationDate())
                .build();

        LocalDateTime originalCreationDate = existingProduct.getCreationDate();

        // When
        productMapper.updateEntityFromDto(updateDto, existingProduct);

        // Then
        assertThat(existingProduct.getId()).isEqualTo("1"); // ID inchange
        assertThat(existingProduct.getName()).isEqualTo("Neu name");
        assertThat(existingProduct.getPrice()).isEqualByComparingTo(new BigDecimal("25.00"));
        assertThat(existingProduct.getCreationDate()).isEqualTo(originalCreationDate); // unchanged name
    }

    @Test
    @DisplayName("should convert list of entities to list of dtos")
    void toDtoList() {
        // Given
        Product product1 = Product.builder()
                .id("1")
                .name("Rosebuch")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("15.99"))
                .stock(50)
                .description("Red climbing rose")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        Product product2 = Product.builder()
                .id("2")
                .name("organic")
                .category(ProductCategory.FERTILIZER)
                .price(new BigDecimal("12.50"))
                .stock(100)
                .description("organic biologique")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        // When
        var dtoList = productMapper.toDtoList(List.of(product1, product2));

        // Then
        assertThat(dtoList).hasSize(2);
    }

    @Test
    @DisplayName("should convert list of dtos to list of entities")
    void toEntityList(){
        ProductDTOResponse productDTOResponse1 = ProductDTOResponse.builder()
                .id("1")
                .name("Rose")
                .category(ProductCategory.PLANT)
                .price(new BigDecimal("15.99"))
                .stock(50)
                .description("Beau rosier rouge")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();
        ProductDTOResponse productDTOResponse2 = ProductDTOResponse.builder()
                .id("2")
                .name("Engrais bio")
                .category(ProductCategory.FERTILIZER)
                .price(new BigDecimal("12.50"))
                .stock(100)
                .description("Engrais biologique")
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        //when
        var entityList = productMapper.toEntityList(List.of(productDTOResponse1, productDTOResponse2));

        // Then
        assertThat(entityList).hasSize(2);
    }


}