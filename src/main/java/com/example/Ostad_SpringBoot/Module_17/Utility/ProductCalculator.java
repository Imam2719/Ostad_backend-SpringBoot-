package com.example.Ostad_SpringBoot.Module_17.Utility;

public class ProductCalculator {

    /**
     * Calculates the final price after applying a discount percentage
     * @param originalPrice the original price
     * @param discountRate the discount rate as a percentage (0-100)
     * @return the discounted price
     */
    public double calculateDiscountedPrice(double originalPrice, double discountRate) {
        if (discountRate < 0 || discountRate > 100) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 100");
        }
        return originalPrice * (1 - discountRate / 100.0);
    }

    /**
     * Checks if the current stock is sufficient for the required quantity
     * @param currentQuantity the available stock quantity
     * @param requiredQuantity the quantity required for the order
     * @return true if stock is sufficient, false otherwise
     */
    public boolean isQuantitySufficient(int currentQuantity, int requiredQuantity) {
        return currentQuantity >= requiredQuantity;
    }
}