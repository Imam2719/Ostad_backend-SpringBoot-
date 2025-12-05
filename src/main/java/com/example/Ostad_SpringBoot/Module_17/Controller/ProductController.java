package com.example.Ostad_SpringBoot.Module_17.Controller;

import com.example.Ostad_SpringBoot.Module_17.Exception.ProductNotFoundException;
import com.example.Ostad_SpringBoot.Module_17.Model.Product;
import com.example.Ostad_SpringBoot.Module_17.Service.ProductManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductManagerService productManagerService;

    @GetMapping("/sku/{sku}")
    public ResponseEntity<Product> findProductBySku(@PathVariable String sku) {
        try {
            Product product = productManagerService.findProductBySku(sku);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{sku}/restock")
    public ResponseEntity<Product> restockProduct(
            @PathVariable String sku,
            @RequestParam int quantity) {
        try {
            Product updatedProduct = productManagerService.restockProduct(sku, quantity);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}