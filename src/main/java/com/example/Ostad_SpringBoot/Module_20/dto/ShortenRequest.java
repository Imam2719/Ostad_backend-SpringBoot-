package com.example.Ostad_SpringBoot.Module_20.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenRequest {

    @NotBlank(message = "Original URL cannot be blank")
    @Pattern(
            regexp = "^(https?://).*",
            message = "URL must start with http:// or https://"
    )
    private String originalUrl;

    @NotNull(message = "Validity cannot be null")
    private String validity;
}
