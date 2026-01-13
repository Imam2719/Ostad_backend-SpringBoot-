package com.example.Ostad_SpringBoot.Module_20.exception;

public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String message) {
        super(message);
    }
}