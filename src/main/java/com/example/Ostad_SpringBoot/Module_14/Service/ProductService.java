package com.example.Ostad_SpringBoot.Module_14.Service;


import com.example.Ostad_SpringBoot.Module_14.Exception.ProductNotFoundException;
import com.example.Ostad_SpringBoot.Module_14.Model.Product;
import com.example.Ostad_SpringBoot.Module_14.Repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ProductService {
    private static final Pattern SKU_PATTERN = Pattern.compile("^SKU-[A-Z0-9]{8}$");

    @Autowired
    private ProductRepository productRepository;

    /**
     * Validates SKU format: must start with "SKU-" followed by 8 alphanumeric characters
     */
    private void validateSkuFormat(String sku) {
        log.debug("Validating SKU format: {}", sku);
        if (sku == null || !SKU_PATTERN.matcher(sku).matches()) {
            log.error("Invalid SKU format: {}", sku);
            throw new InvalidSkuFormatException("SKU must start with 'SKU-' followed by 8 alphanumeric characters (e.g., SKU-A1B2C3D4)");
        }
    }

    /**
     * Validates SKU uniqueness
     */
    private void validateSkuUniqueness(String sku) {
        log.debug("Checking SKU uniqueness: {}", sku);
        if (productRepository.existsBySku(sku)) {
            log.error("SKU already exists: {}", sku);
            throw new SkuAlreadyExistsException("Product with SKU '" + sku + "' already exists");
        }
    }

    /**
     * Creates a new product
     */
    public Product createProduct(Product product) {
        log.info("Creating product with SKU: {}", product.getSku());

        // Validate SKU format
        validateSkuFormat(product.getSku());

        // Validate SKU uniqueness
        validateSkuUniqueness(product.getSku());

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {} and SKU: {}", savedProduct.getId(), savedProduct.getSku());

        return savedProduct;
    }

    /**
     * Retrieves all products
     */
    public List<Product> getAllProducts() {
        log.info("Retrieving all products");
        List<Product> products = productRepository.findAll();
        log.debug("Found {} products", products.size());
        return products;
    }

    /**
     * Retrieves a product by ID
     */
    public Product getProductById(Long id) {
        log.info("Retrieving product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {}", id);
                    return new ProductNotFoundException("Product with ID " + id + " not found");
                });
        log.debug("Product found: {}", product.getSku());
        return product;
    }

    /**
     * Updates an existing product
     */
    public Product updateProduct(Long id, Product productDetails) {
        log.info("Updating product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {} for update", id);
                    return new ProductNotFoundException("Product with ID " + id + " not found");
                });

        // Check if SKU is being changed
        if (!existingProduct.getSku().equals(productDetails.getSku())) {
            log.error("Cannot change SKU of existing product. Old: {}, New: {}",
                    existingProduct.getSku(), productDetails.getSku());
            throw new IllegalArgumentException("SKU of an existing product cannot be changed");
        }

        // Update fields
        existingProduct.setName(productDetails.getName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setStatus(productDetails.getStatus());

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Product updated with ID: {}", updatedProduct.getId());

        return updatedProduct;
    }

    /**
     * Deletes a product by ID
     */
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to find product with ID: {} for deletion", id);
                    return new ProductNotFoundException("Product with ID " + id + " not found");
                });

        productRepository.delete(product);
        log.info("Product deleted with ID: {} and SKU: {}", id, product.getSku());
    }
}

// Custom Exception Classes (in the same file for convenience, but should be separate files)
class InvalidSkuFormatException extends RuntimeException {
    public InvalidSkuFormatException(String message) {
        super(message);
    }
}

class SkuAlreadyExistsException extends RuntimeException {
    public SkuAlreadyExistsException(String message) {
        super(message);
    }
}