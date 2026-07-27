package de0.backend.gartenmanagement.repository;


import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;


@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    private Product rosebush;
    private Product mower;
    private Product fertilizer;

    @BeforeEach
    void setUp() {
        rosebush = Product.builder()
                .name("rosebush")
                .description("Red climbing rose")
                .price(new BigDecimal("15.99"))
                .category(ProductCategory.PLANT)
                .stock(50)
                .active(true)
                .build();

        mower = Product.builder()
                .name("mower")
                .description("Electric lawn mower")
                .price(new BigDecimal("299.99"))
                .category(ProductCategory.TOOL)
                .stock(5)
                .active(true)
                .build();

        fertilizer = Product.builder()
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


}