package de0.backend.gartenmanagement.repository;


import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@DisplayName("ProductRepository Test")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    void setUp() {
        Product rosebush = Product.builder()
                .name("rosebush")
                .description("Red climbing rose")
                .price(new BigDecimal("15.99"))
                .category(ProductCategory.PLANT)
                .stock(50)
                .active(true)
                .build();

        Product mower = Product.builder()
                .name("mower")
                .description("Electric lawn mower")
                .price(new BigDecimal("299.99"))
                .category(ProductCategory.TOOL)
                .stock(5)
                .active(true)
                .build();

        Product fertilizer = Product.builder()
                .name("bio fertilizer")
                .description("Organic fertilizer 5 kg")
                .price(new BigDecimal("12.55"))
                .category(ProductCategory.FERTILIZER)
                .stock(0)
                .active(false)
                .build();

        entityManager.persist(rosebush);
        entityManager.persist(mower);
        entityManager.persist(fertilizer);
        entityManager.flush();
    }

    @Test
    @DisplayName("save product with success")
    void save_product_with_success() {
        //Given
        Product newProduct = Product.builder()
                .name("tulip")
                .description("Yellow tulip")
                .price(new BigDecimal("5.99"))
                .category(ProductCategory.PLANT)
                .stock(100)
                .active(true)
                .build();
        //when
        Product savedProduct = productRepository.save(newProduct);

        //then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getCreationDate()).isNotNull();
    }


    @Test
    @DisplayName("find product by category")
    void find_product_By_Category() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<Product> products = productRepository.findByCategory(ProductCategory.PLANT, pageRequest);
        Page<Product> products1 = productRepository.findByCategory(ProductCategory.TOOL, pageRequest);
        Page<Product> products2 = productRepository.findByCategory(ProductCategory.FERTILIZER, pageRequest);

        // Then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(1);
        assertThat(products.getContent())
                .hasSize(1)
                .first()
                .extracting(Product::getName, Product::getCategory)
                .containsExactly("rosebush", ProductCategory.PLANT);

        assertThat(products1).isNotNull();
        assertThat(products1.getTotalElements()).isEqualTo(1);
        assertThat(products1.getContent())
                .hasSize(1)
                .first()
                .extracting(Product::getName, Product::getCategory)
                .containsExactly("mower", ProductCategory.TOOL);

        assertThat(products2).isNotNull();
        assertThat(products2.getTotalElements()).isEqualTo(1);
        assertThat(products2.getContent())
                .hasSize(1)
                .first()
                .extracting(Product::getName, Product::getCategory)
                .containsExactly("bio fertilizer", ProductCategory.FERTILIZER);
    }
}