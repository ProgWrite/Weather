package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponseDto(
        String name,
        double lat,
        double lon,
        String country,
        double temp,
        String description,
        double humidity,
        double feelsLike,
        String icon
        ) {

    private static final double KELVIN_OFFSET = 273.15;

    public static WeatherResponseDto fromJson(JsonNode rootNode, LocationResponseDto location) {
        return new WeatherResponseDto(
                location.name(),
                location.lat(),
                location.lon(),
                rootNode.path("sys").path("country").asText(),
                rootNode.path("main").path("temp").asDouble(),
                rootNode.path("weather").get(0).path("description").asText(),
                rootNode.path("main").path("humidity").asDouble(),
                rootNode.path("main").path("feels_like").asDouble(),
                rootNode.path("weather").get(0).path("icon").asText()
        );
    }

    public double getTempCelsiusRounded() {
        return Math.round(temp - KELVIN_OFFSET);
    }

    public double getFeelsLikeCelsiusRounded() {
        return Math.round(feelsLike - KELVIN_OFFSET);
    }

}
