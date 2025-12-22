package com.example.Ostad_SpringBoot.Module_18.Controller;

import com.example.Ostad_SpringBoot.Module_18.Service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    @Autowired
    private WelcomeService welcomeService;

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("message", welcomeService.getMessage());
        response.put("apiUrl", welcomeService.getApiUrl());
        return response;
    }
}