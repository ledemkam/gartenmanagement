package de0.backend.gartenmanagement.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import de0.backend.gartenmanagement.common.PageResponse;
import de0.backend.gartenmanagement.dtos.ProductDTORequest;
import de0.backend.gartenmanagement.dtos.ProductDTOResponse;
import de0.backend.gartenmanagement.entities.ProductCategory;
import de0.backend.gartenmanagement.services.catalog.ProductService;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(ProductController.class)
@DisplayName("Integration test for ProductController")
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should create a new product and return 201 Created with Location header")
    void should_Create_Product() throws Exception{
        // Given
        ProductDTORequest createDto = ProductDTORequest.builder()
                 .name("Nouveau Produit")
                 .category(ProductCategory.TOOL)
                 .description("Description du produit")
                 .price(new BigDecimal("25.00"))
                 .stock(100)
         .build();

         ProductDTOResponse createdProduct = ProductDTOResponse.builder()
                 .id("1")
                 .name("Nouveau Produit")
                 .category(ProductCategory.TOOL)
                 .description("Description du produit")
                 .price(new BigDecimal("25.00"))
                 .stock(100)
                 .active(true)
                 .build();

         when(productService.create(any())).thenReturn(
                createdProduct);

         // When & Then
          mockMvc.perform(post("/api/v1/products")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(
                         createDto)))
          .andExpect(status().isCreated())
          .andExpect(header().exists("Location"))
          .andExpect(jsonPath("$.id", is("1")))
          .andExpect(jsonPath("$.name", is("Nouveau Produit")));

    }

    @Test
    @DisplayName("Should get Product by Id")
    void should_get_Product_By_Id() throws Exception {
        // Given
        String productId = "1";
        ProductDTOResponse existingProduct = ProductDTOResponse.builder()
                .id(productId)
                .name("Nouveau Produit")
                .category(ProductCategory.TOOL)
                .description("Description du produit")
                .price(new BigDecimal("25.00"))
                .stock(100)
                .active(true)
                .build();

        when(productService.findById(productId)).thenReturn(existingProduct);

        // When & Then
        mockMvc.perform(get("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Nouveau Produit")))
                .andExpect(jsonPath("$.category", is(ProductCategory.TOOL.name())))
                .andExpect(jsonPath("$.description", is("Description du produit")))
                .andExpect(jsonPath("$.stock", is(100)))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    @DisplayName("Should update a product")
    void should_Update_Product() throws Exception {
        // Given
        String productId = "1";
        ProductDTORequest updateDto = ProductDTORequest.builder()
                .name("Produit MAJ")
                .category(ProductCategory.TOOL)
                .description("Description mise a jour")
                .price(new BigDecimal("19.99"))
                .stock(80)
                .build();

        ProductDTOResponse updatedProduct = ProductDTOResponse.builder()
                .id(productId)
                .name("Produit MAJ")
                .category(ProductCategory.TOOL)
                .description("Description mise a jour")
                .price(new BigDecimal("19.99"))
                .stock(80)
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        when(productService.update(any(), any())).thenReturn(updatedProduct);

        // When & Then
        mockMvc.perform(put("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Produit MAJ")))
                .andExpect(jsonPath("$.stock", is(80)));
    }

    @Test
    @DisplayName("Should get all products")
    void should_Get_All_Products() throws Exception {
        // Given
        ProductDTOResponse product = ProductDTOResponse.builder()
                .id("1")
                .name("Produit A")
                .category(ProductCategory.TOOL)
                .description("Description A")
                .price(new BigDecimal("10.00"))
                .stock(12)
                .active(true)
                .creationDate(LocalDateTime.now())
                .build();

        PageResponse<ProductDTOResponse> pageResponse = PageResponse.<ProductDTOResponse>builder()
                .content(List.of(product))
                .page(0)
                .size(10)
                .totalElements(1)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .isFirst(true)
                .isLast(true)
                .build();

        when(productService.findAll(any())).thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is("1")))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

}