package com.example.Ostad_SpringBoot.Module_17.Service;

import com.example.Ostad_SpringBoot.Module_17.Exception.ProductNotFoundException;
import com.example.Ostad_SpringBoot.Module_17.Model.Product;
import com.example.Ostad_SpringBoot.Module_17.Repositories.ProductRepository;
import com.example.Ostad_SpringBoot.Module_17.Service.ProductManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("ProductManagerService - Service Layer Business Logic Testing")
@ExtendWith(MockitoExtension.class)
class ProductManagerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManagerService productManagerService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("TEST-SKU-001");
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setQuantity(100);
        testProduct.setDescription("Test Description");
    }

    // Tests for findProductBySku

    @Test
    @DisplayName("Should successfully find product when SKU exists")
    void testFindProductBySkuSuccess() {
        // Arrange
        when(productRepository.findBySku("TEST-SKU-001"))
                .thenReturn(Optional.of(testProduct));

        // Act
        Product result = productManagerService.findProductBySku("TEST-SKU-001");

        // Assert
        assertNotNull(result, "Product should not be null");
        assertEquals("TEST-SKU-001", result.getSku());
        assertEquals("Test Product", result.getName());
        assertEquals(100, result.getQuantity());

        // Verify
        verify(productRepository, times(1)).findBySku("TEST-SKU-001");
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when SKU doesn't exist")
    void testFindProductBySkuNotFound() {
        // Arrange
        when(productRepository.findBySku(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productManagerService.findProductBySku("INVALID-SKU"),
                "Should throw ProductNotFoundException for non-existent SKU"
        );

        assertTrue(exception.getMessage().contains("INVALID-SKU"));
        assertTrue(exception.getMessage().contains("not found"));

        // Verify
        verify(productRepository, times(1)).findBySku("INVALID-SKU");
    }

    // Tests for restockProduct

    @Test
    @DisplayName("Should successfully restock product and update quantity")
    void testRestockProductSuccess() {
        // Arrange
        int initialQuantity = testProduct.getQuantity();
        int quantityToAdd = 50;

        when(productRepository.findBySku("TEST-SKU-001"))
                .thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct))
                .thenReturn(testProduct);

        // Act
        Product result = productManagerService.restockProduct("TEST-SKU-001", quantityToAdd);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(initialQuantity + quantityToAdd, result.getQuantity());
        assertEquals(150, result.getQuantity());

        // Verify repository.save() was called exactly once with updated object
        verify(productRepository, times(1)).findBySku("TEST-SKU-001");
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Should throw exception when attempting to restock non-existent product")
    void testRestockProductNotFound() {
        // Arrange
        when(productRepository.findBySku(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productManagerService.restockProduct("INVALID-SKU", 50),
                "Should throw ProductNotFoundException for non-existent product"
        );

        // Verify
        verify(productRepository, times(1)).findBySku("INVALID-SKU");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should correctly update quantity with zero addition")
    void testRestockProductWithZeroQuantity() {
        // Arrange
        int initialQuantity = testProduct.getQuantity();

        when(productRepository.findBySku("TEST-SKU-001"))
                .thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct))
                .thenReturn(testProduct);

        // Act
        Product result = productManagerService.restockProduct("TEST-SKU-001", 0);

        // Assert
        assertEquals(initialQuantity, result.getQuantity());
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    @DisplayName("Should correctly update quantity with large quantities")
    void testRestockProductWithLargeQuantity() {
        // Arrange
        int largeQuantity = 1000;

        when(productRepository.findBySku("TEST-SKU-001"))
                .thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct))
                .thenReturn(testProduct);

        // Act
        Product result = productManagerService.restockProduct("TEST-SKU-001", largeQuantity);

        // Assert
        assertEquals(1100, result.getQuantity());
        verify(productRepository, times(1)).save(testProduct);
    }
}