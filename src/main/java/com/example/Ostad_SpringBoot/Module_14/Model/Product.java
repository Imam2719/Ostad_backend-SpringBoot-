package com.example.Ostad_SpringBoot.Module_14.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Description must be no longer than 500 characters")
    private String description;

    @NotBlank(message = "SKU must not be blank")
    @Column(nullable = false, unique = true)
    private String sku;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive number")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be zero or more")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    // Constructors
    public Product() {
    }

    public Product(String name, String description, String sku, Double price, Integer quantity, ProductStatus status) {
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}