package com.example.Ostad_SpringBoot.Module_20.Controller;

import com.example.Ostad_SpringBoot.Module_20.Service.UrlService;
import com.example.Ostad_SpringBoot.Module_20.dto.RetrieveResponse;
import com.example.Ostad_SpringBoot.Module_20.dto.ShortenRequest;
import com.example.Ostad_SpringBoot.Module_20.dto.ShortenResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shortenUrl(@Valid @RequestBody ShortenRequest request) {
        ShortenResponse response = urlService.shortenUrl(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/r/{shortUrl}")
    public ResponseEntity<RetrieveResponse> retrieveUrl(@PathVariable String shortUrl) {
        RetrieveResponse response = urlService.retrieveOriginalUrl(shortUrl);
        return ResponseEntity.ok(response);
    }
}