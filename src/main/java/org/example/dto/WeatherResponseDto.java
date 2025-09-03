package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponseDto(
        String name,
        String country,
        double temp,
        String description,
        double humidity) {

    private static final double KELVIN_OFFSET = 273.15;

    public static WeatherResponseDto fromJson(JsonNode rootNode, String locationName) {
        return new WeatherResponseDto(
//                rootNode.path("name").asText(),
                locationName,
                rootNode.path("sys").path("country").asText(),
                rootNode.path("main").path("temp").asDouble(),
                rootNode.path("weather").get(0).path("description").asText(),
                rootNode.path("main").path("humidity").asDouble()
        );
    }

    public double getTempCelsiusRounded() {
        return Math.round(temp - KELVIN_OFFSET);
    }
}
