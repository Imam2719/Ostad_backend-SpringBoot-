package com.example.Ostad_SpringBoot.Module_17.Service;

import com.example.Ostad_SpringBoot.Module_17.Exception.ProductNotFoundException;
import com.example.Ostad_SpringBoot.Module_17.Model.Product;
import com.example.Ostad_SpringBoot.Module_17.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductManagerService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Finds a product by its SKU
     * @param sku the product SKU
     * @return the Product if found
     * @throws ProductNotFoundException if product with given SKU doesn't exist
     */
    public Product findProductBySku(String sku) {
        Optional<Product> product = productRepository.findBySku(sku);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with SKU '" + sku + "' not found");
        }
        return product.get();
    }

    /**
     * Restocks a product by increasing its quantity
     * @param sku the product SKU
     * @param quantityToAdd the quantity to add to stock
     * @return the updated Product
     * @throws ProductNotFoundException if product with given SKU doesn't exist
     */
    public Product restockProduct(String sku, int quantityToAdd) {
        Product product = findProductBySku(sku);
        product.setQuantity(product.getQuantity() + quantityToAdd);
        return productRepository.save(product);
    }
}