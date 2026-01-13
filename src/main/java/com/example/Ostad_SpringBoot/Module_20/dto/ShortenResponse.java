package com.example.Ostad_SpringBoot.Module_20.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShortenResponse {

    private String shortUrl;
    private String originalUrl;
    private String expiresAt;
}