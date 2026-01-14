package com.example.Ostad_SpringBoot.Module_21.Service;

import com.example.Ostad_SpringBoot.Module_21.repository.WeatherApiResponse;
import com.example.Ostad_SpringBoot.Module_21.repository.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
    }

    public WeatherResponse getWeatherByCity(String city) {
        logger.info("Fetching weather data for city: {}", city);

        try {
            // Build the URL with query parameters
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/current.json")
                    .queryParam("key", apiKey)
                    .queryParam("q", city)
                    .queryParam("aqi", "no")
                    .build()
                    .toUriString();

            logger.debug("Calling Weather API: {}", url.replace(apiKey, "***"));

            // Call external API
            WeatherApiResponse apiResponse = restTemplate.getForObject(url, WeatherApiResponse.class);

            if (apiResponse == null) {
                throw new RuntimeException("No response from Weather API");
            }

            // Map to our response format
            WeatherResponse response = new WeatherResponse();
            response.setCity(apiResponse.getLocation().getName());
            response.setCountry(apiResponse.getLocation().getCountry());
            response.setLatitude(apiResponse.getLocation().getLat());
            response.setLongitude(apiResponse.getLocation().getLon());
            response.setTemperature(apiResponse.getCurrent().getTemp_c());
            response.setWind(apiResponse.getCurrent().getWind_kph());
            response.setHumidity(apiResponse.getCurrent().getHumidity());
            response.setCondition(apiResponse.getCurrent().getCondition().getText());
            response.setLocalTime(apiResponse.getLocation().getLocaltime());

            logger.info("Successfully fetched weather data for {}", city);
            return response;

        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {}", city, e);
            throw new RuntimeException("Failed to fetch weather data: " + e.getMessage());
        }
    }
}