package com.example.Ostad_SpringBoot.Module_17.Service;

import com.example.Ostad_SpringBoot.Module_17.Model.Product;
import com.example.Ostad_SpringBoot.Module_17.Repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Product Inventory System - Integration Tests")
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        testProduct = new Product();
        testProduct.setSku("LAPTOP-001");
        testProduct.setName("Gaming Laptop");
        testProduct.setPrice(1299.99);
        testProduct.setQuantity(50);
        testProduct.setDescription("High-performance gaming laptop");

        productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Should retrieve product by SKU successfully")
    void testGetProductBySkuSuccess() throws Exception {
        mockMvc.perform(get("/api/products/sku/LAPTOP-001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", equalTo("LAPTOP-001")))
                .andExpect(jsonPath("$.name", equalTo("Gaming Laptop")))
                .andExpect(jsonPath("$.price", equalTo(1299.99)))
                .andExpect(jsonPath("$.quantity", equalTo(50)));
    }

    @Test
    @DisplayName("Should return 404 when product SKU doesn't exist")
    void testGetProductBySkuNotFound() throws Exception {
        mockMvc.perform(get("/api/products/sku/NON-EXISTENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should restock product and update quantity correctly")
    void testRestockProductSuccess() throws Exception {
        mockMvc.perform(put("/api/products/LAPTOP-001/restock")
                        .param("quantity", "30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", equalTo("LAPTOP-001")))
                .andExpect(jsonPath("$.quantity", equalTo(80)));
    }

    @Test
    @DisplayName("Should return 404 when restocking non-existent product")
    void testRestockProductNotFound() throws Exception {
        mockMvc.perform(put("/api/products/NON-EXISTENT/restock")
                        .param("quantity", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle multiple restock operations correctly")
    void testMultipleRestockOperations() throws Exception {
        // First restock
        mockMvc.perform(put("/api/products/LAPTOP-001/restock")
                        .param("quantity", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", equalTo(75)));

        // Second restock
        mockMvc.perform(put("/api/products/LAPTOP-001/restock")
                        .param("quantity", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", equalTo(100)));
    }

    @Test
    @DisplayName("Should verify data persistence after restock")
    void testDataPersistenceAfterRestock() throws Exception {
        mockMvc.perform(put("/api/products/LAPTOP-001/restock")
                .param("quantity", "50"));

        // Verify by fetching the product again
        mockMvc.perform(get("/api/products/sku/LAPTOP-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", equalTo(100)));
    }
}