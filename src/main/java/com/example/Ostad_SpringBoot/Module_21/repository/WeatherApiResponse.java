package com.example.Ostad_SpringBoot.Module_21.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponse {
    private Location location;
    private Current current;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private Double lat;
        private Double lon;
        private String name;
        private String region;
        private String country;
        private String localtime;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        private Double temp_c;
        private Double wind_kph;
        private Double humidity;
        private Condition condition;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Condition {
        private String text;
    }
}