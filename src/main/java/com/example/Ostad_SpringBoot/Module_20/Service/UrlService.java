package com.example.Ostad_SpringBoot.Module_20.Service;

import com.example.Ostad_SpringBoot.Module_20.dto.RetrieveResponse;
import com.example.Ostad_SpringBoot.Module_20.dto.ShortenRequest;
import com.example.Ostad_SpringBoot.Module_20.dto.ShortenResponse;
import com.example.Ostad_SpringBoot.Module_20.entity.Url;
import com.example.Ostad_SpringBoot.Module_20.exception.UrlExpiredException;
import com.example.Ostad_SpringBoot.Module_20.exception.UrlNotFoundException;
import com.example.Ostad_SpringBoot.Module_20.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Value("${server.port:8080}")
    private String serverPort;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_URL_LENGTH = 7;
    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Transactional
    public ShortenResponse shortenUrl(ShortenRequest request) {
        // Check if the URL already exists in database
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(request.getOriginalUrl());
        if (existingUrl.isPresent()) {
            throw new IllegalArgumentException("URL already exists in the database");
        }

        // Parse validity to LocalDateTime
        LocalDateTime expiresAt = parseValidity(request.getValidity());

        // Validate that expiry is in the future
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Validity must be in the future");
        }

        // Generate unique short URL
        String shortUrl = generateUniqueShortUrl();

        // Create and save the URL entity
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setShortUrl(shortUrl);
        url.setExpiresAt(expiresAt);

        Url savedUrl = urlRepository.save(url);

        // Prepare response
        ShortenResponse response = new ShortenResponse();
        response.setShortUrl("http://localhost:" + serverPort + "/r/" + savedUrl.getShortUrl());
        response.setOriginalUrl(savedUrl.getOriginalUrl());
        response.setExpiresAt(savedUrl.getExpiresAt().format(formatter));

        return response;
    }

    @Transactional(readOnly = true)
    public RetrieveResponse retrieveOriginalUrl(String shortUrl) {
        // Find URL by short code
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

        // Check if URL has expired
        if (url.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlExpiredException("url expired");
        }

        // Prepare response
        RetrieveResponse response = new RetrieveResponse();
        response.setOriginalUrl(url.getOriginalUrl());
        response.setExpiresAt(url.getExpiresAt().format(formatter));

        return response;
    }

    private String generateUniqueShortUrl() {
        String shortUrl;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            shortUrl = generateShortUrl();
            attempts++;
            if (attempts > maxAttempts) {
                throw new RuntimeException("Unable to generate unique short URL after " + maxAttempts + " attempts");
            }
        } while (urlRepository.existsByShortUrl(shortUrl));

        return shortUrl;
    }

    private String generateShortUrl() {
        StringBuilder sb = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    private LocalDateTime parseValidity(String validity) {
        try {
            // Try to parse ISO format: 2026-01-15T23:59:59
            return LocalDateTime.parse(validity, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            try {
                // Try alternative format: 2026-01-15 23:59:59
                return LocalDateTime.parse(validity, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Invalid validity format. Use format: yyyy-MM-ddTHH:mm:ss or yyyy-MM-dd HH:mm:ss");
            }
        }
    }
}