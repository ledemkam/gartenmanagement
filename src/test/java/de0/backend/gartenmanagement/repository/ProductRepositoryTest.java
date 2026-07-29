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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@DisplayName("ProductRepository Test")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    PageRequest pageRequest = PageRequest.of(0, 10);


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

    @Test
    @DisplayName("find only active products")
    void find_product_with_active_true(){
        //when
        Page<Product> products = productRepository.findByActiveTrue(pageRequest);

        //then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getContent())
                .hasSize(2)
                .extracting(Product::getActive)
                .containsExactly(true,true);

    }

    @Test
    @DisplayName("should find products between price range")
    void find_products_betwen_price() {
        //when
        Page<Product> products = productRepository.findByPriceBetween(
                new BigDecimal("10.00"),
                new BigDecimal("20.00"), pageRequest);

        //then
        assertThat(products).isNotNull();
        assertThat(products.getTotalElements()).isEqualTo(2);
        assertThat(products.getContent())
                .hasSize(2)
                .extracting(Product::getPrice)
                .containsExactly(new BigDecimal("15.99"), new BigDecimal("12.55"));
    }

    @Test
    @DisplayName("find product by category and active true")
    void find_by_category_and_active_true() {
        //when
        Page<Product> activePlants = productRepository.findByCategoryAndActiveTrue(ProductCategory.PLANT, pageRequest);
        Page<Product> activeFertilizers = productRepository.findByCategoryAndActiveTrue(ProductCategory.FERTILIZER, pageRequest);

        //then
        assertThat(activePlants).isNotNull();
        assertThat(activePlants.getTotalElements()).isEqualTo(1);
        assertThat(activePlants.getContent())
                .extracting(Product::getName)
                .containsExactly("rosebush");

        assertThat(activeFertilizers).isNotNull();
        assertThat(activeFertilizers.getTotalElements()).isZero();
        assertThat(activeFertilizers.getContent()).isEmpty();
    }

    @Test
    @DisplayName("find product by name ignore case")
    void find_by_name_ignore_case() {
        //when
        Optional<Product> product = productRepository.findByNameIgnoreCase("RoSeBuSh");

        //then
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("rosebush");
    }

    @Test
    @DisplayName("count products by category")
    void count_by_category() {
        //when
        Long plantsCount = productRepository.countByCategory(ProductCategory.PLANT);
        Long toolsCount = productRepository.countByCategory(ProductCategory.TOOL);
        Long fertilizersCount = productRepository.countByCategory(ProductCategory.FERTILIZER);

        //then
        assertThat(plantsCount).isEqualTo(1L);
        assertThat(toolsCount).isEqualTo(1L);
        assertThat(fertilizersCount).isEqualTo(1L);
    }

    @Test
    @DisplayName("find low stock active products")
    void find_low_stock_products() {
        //when
        Page<Product> lowStockProducts = productRepository.findLowStockProducts(10, pageRequest);

        //then
        assertThat(lowStockProducts).isNotNull();
        assertThat(lowStockProducts.getTotalElements()).isEqualTo(1);
        assertThat(lowStockProducts.getContent())
                .hasSize(1)
                .first()
                .extracting(Product::getName, Product::getStock, Product::getActive)
                .containsExactly("mower", 5, true);
    }

    @Test
    @DisplayName("find all active categories")
    void find_all_active_categories() {
        //when
        Page<ProductCategory> activeCategories = productRepository.findAllActiveCategories(pageRequest);

        //then
        assertThat(activeCategories).isNotNull();
        assertThat(activeCategories.getTotalElements()).isEqualTo(2);
        assertThat(activeCategories.getContent())
                .containsExactlyInAnyOrder(ProductCategory.PLANT, ProductCategory.TOOL);
    }

    @Test
    @DisplayName("find available products above price")
    void delete_product_by_id() {
        // Given
        Product product = Product.builder()
                .name("thym")
                .description("Garden thym")
                .price(new BigDecimal("25.99"))
                .category(ProductCategory.TOOL)
                .stock(10)
                .active(true)
                .build();
        Product savedProduct = productRepository.save(product);
        String productId = savedProduct.getId();
         // When
        productRepository.deleteById(productId);
        Optional<Product> deleted = productRepository.findById(productId);
         // Then
        assertThat(deleted).isEmpty();
    }
}