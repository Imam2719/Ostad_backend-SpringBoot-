package com.example.Ostad_SpringBoot.Module_21.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("temperature_celsius")
    private Double temperature;

    @JsonProperty("wind_kph")
    private Double wind;

    @JsonProperty("humidity_percent")
    private Double humidity;

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("local_time")
    private String localTime;
}