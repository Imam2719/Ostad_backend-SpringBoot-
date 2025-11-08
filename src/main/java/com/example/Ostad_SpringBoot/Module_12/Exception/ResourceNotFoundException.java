package com.example.Ostad_SpringBoot.Module_12.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}