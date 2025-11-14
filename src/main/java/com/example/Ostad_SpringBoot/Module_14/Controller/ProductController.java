package com.example.Ostad_SpringBoot.Module_14.Controller;


import com.example.Ostad_SpringBoot.Module_14.Model.Product;
import com.example.Ostad_SpringBoot.Module_14.Service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * POST /api/products - Creates a new product
     * Returns 201 Created on success
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        log.info("Received request to create product: {}", product.getSku());
        log.debug("Request body: name={}, sku={}, price={}, quantity={}",
                product.getName(), product.getSku(), product.getPrice(), product.getQuantity());

        Product createdProduct = productService.createProduct(product);

        log.info("Product created with ID: {} and SKU: {}", createdProduct.getId(), createdProduct.getSku());
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * GET /api/products - Returns a list of all products
     * Returns 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Received request to get all products");

        List<Product> products = productService.getAllProducts();

        log.info("Returning {} products", products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id} - Returns a single product by its ID
     * Returns 200 OK if found, 404 Not Found otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("Received request to get product by ID: {}", id);

        Product product = productService.getProductById(id);

        log.info("Returning product with ID: {} and SKU: {}", product.getId(), product.getSku());
        return ResponseEntity.ok(product);
    }

    /**
     * PUT /api/products/{id} - Updates an existing product
     * Returns 200 OK on success, 404 Not Found if product doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product productDetails) {
        log.info("Received request to update product with ID: {}", id);
        log.debug("Update request body: name={}, sku={}, price={}, quantity={}",
                productDetails.getName(), productDetails.getSku(),
                productDetails.getPrice(), productDetails.getQuantity());

        Product updatedProduct = productService.updateProduct(id, productDetails);

        log.info("Product updated with ID: {}", updatedProduct.getId());
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * DELETE /api/products/{id} - Deletes a product by its ID
     * Returns 204 No Content on success, 404 Not Found if product doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Received request to delete product with ID: {}", id);

        productService.deleteProduct(id);

        log.info("Product deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}