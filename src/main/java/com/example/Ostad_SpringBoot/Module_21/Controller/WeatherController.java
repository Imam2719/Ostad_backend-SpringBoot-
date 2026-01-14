package com.example.Ostad_SpringBoot.Module_21.Controller;

import com.example.Ostad_SpringBoot.Module_21.Service.WeatherService;
import com.example.Ostad_SpringBoot.Module_21.repository.WeatherResponse;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@Validated
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam @NotBlank(message = "City name is required") String city) {

        logger.info("Received request for weather data - City: {}", city);

        WeatherResponse weatherData = weatherService.getWeatherByCity(city.trim());
        logger.info("Successfully returned weather data for: {}", city);

        return ResponseEntity.ok(weatherData);
    }
}