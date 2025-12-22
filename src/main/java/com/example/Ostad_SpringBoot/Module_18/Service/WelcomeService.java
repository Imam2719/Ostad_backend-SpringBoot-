package com.example.Ostad_SpringBoot.Module_18.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    @Value("${app.message}")
    private String message;

    @Value("${app.api.url}")
    private String apiUrl;

    public String getMessage() {
        return message;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}