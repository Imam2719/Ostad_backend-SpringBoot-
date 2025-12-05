package com.example.Ostad_SpringBoot.Module_17.Service.Utility;

import com.example.Ostad_SpringBoot.Module_17.Utility.ProductCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductCalculator - Core Utility Logic Testing")
class ProductCalculatorTest {

    private ProductCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ProductCalculator();
    }

    // Tests for calculateDiscountedPrice

    @Test
    @DisplayName("Should calculate correct price with 0% discount")
    void testNoDiscount() {
        double result = calculator.calculateDiscountedPrice(100.0, 0);
        assertEquals(100.0, result, "Price should remain unchanged with 0% discount");
    }

    @Test
    @DisplayName("Should calculate correct price with 50% discount")
    void testFiftyPercentDiscount() {
        double result = calculator.calculateDiscountedPrice(100.0, 50);
        assertEquals(50.0, result, "50% discount should result in half price");
    }

    @Test
    @DisplayName("Should calculate correct price with 100% discount (full price off)")
    void testFullDiscount() {
        double result = calculator.calculateDiscountedPrice(100.0, 100);
        assertEquals(0.0, result, "100% discount should result in 0 price");
    }

    @Test
    @DisplayName("Should handle decimal prices correctly")
    void testDecimalPriceWithDiscount() {
        double result = calculator.calculateDiscountedPrice(99.99, 25);
        assertEquals(74.9925, result, 0.001, "Should calculate decimal prices correctly");
    }

    @Test
    @DisplayName("Should throw exception for negative discount rate")
    void testNegativeDiscountRate() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscountedPrice(100.0, -10),
                "Should throw exception for negative discount rate");
    }

    @Test
    @DisplayName("Should throw exception for discount rate over 100")
    void testDiscountRateOver100() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscountedPrice(100.0, 150),
                "Should throw exception for discount rate over 100");
    }

    // Tests for isQuantitySufficient

    @Test
    @DisplayName("Should return true when quantity is sufficient")
    void testSufficientQuantity() {
        boolean result = calculator.isQuantitySufficient(50, 30);
        assertTrue(result, "Should return true when available quantity >= required quantity");
    }

    @Test
    @DisplayName("Should return true when quantity exactly matches requirement")
    void testExactQuantityMatch() {
        boolean result = calculator.isQuantitySufficient(50, 50);
        assertTrue(result, "Should return true when quantities are equal");
    }

    @Test
    @DisplayName("Should return false when quantity is insufficient")
    void testInsufficientQuantity() {
        boolean result = calculator.isQuantitySufficient(20, 30);
        assertFalse(result, "Should return false when available quantity < required quantity");
    }

    @Test
    @DisplayName("Should handle zero quantity requirement")
    void testZeroQuantityRequirement() {
        boolean result = calculator.isQuantitySufficient(50, 0);
        assertTrue(result, "Should return true when no quantity is required");
    }

    @Test
    @DisplayName("Should handle zero available quantity")
    void testZeroAvailableQuantity() {
        boolean result = calculator.isQuantitySufficient(0, 1);
        assertFalse(result, "Should return false when no stock is available");
    }
}