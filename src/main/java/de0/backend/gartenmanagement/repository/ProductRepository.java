package de0.backend.gartenmanagement.repository;

import de0.backend.gartenmanagement.entities.Product;
import de0.backend.gartenmanagement.entities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByCategory(ProductCategory category, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByActiveFalse(Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal minPrice,
                                     BigDecimal maxPrice,
                                     Pageable pageable);

    Page<Product> findByCategoryAndActiveTrue(ProductCategory category,
                                              Pageable pageable);

    Optional<Product> findByNameIgnoreCase(String name);

    Long countByCategory(ProductCategory category);
    Boolean existsByNameIgnoreCase(String name);

    @Query("SELECT p FROM Product p WHERE p.stock < :threshold AND p.active = true")
    Page<Product> findLowStockProducts(
                               @Param("threshold") Integer threshold,
                               Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category = :category ORDER BY p.price DESC")
                  Page<Product> findByCategoryOrderByPriceDesc(
                          @Param("category") ProductCategory category,
                          Pageable pageable);

    @Query("SELECT DISTINCT p.category FROM Product p WHERE p.active = true")
          Page<ProductCategory> findAllActiveCategories(Pageable pageable);


    @Query(value = "SELECT * FROM products WHERE price > :minPrice AND stock > 0", nativeQuery = true)
    Page<Product> findAvailableProductsAbovePrice(
            @Param("minPrice") BigDecimal minPrice,
            Pageable pageable);
}
