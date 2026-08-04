package de0.backend.gartenmanagement.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
}