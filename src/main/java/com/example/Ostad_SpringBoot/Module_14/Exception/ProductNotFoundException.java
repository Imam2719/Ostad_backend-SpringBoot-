package com.example.Ostad_SpringBoot.Module_14.Exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

// InvalidSkuFormatException.java
class InvalidSkuFormatException extends RuntimeException {
    public InvalidSkuFormatException(String message) {
        super(message);
    }
}

// SkuAlreadyExistsException.java
class SkuAlreadyExistsException extends RuntimeException {
    public SkuAlreadyExistsException(String message) {
        super(message);
    }
}