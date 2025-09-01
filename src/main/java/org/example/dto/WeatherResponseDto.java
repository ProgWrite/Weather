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

    public static WeatherResponseDto fromJson(JsonNode rootNode) {
        return new WeatherResponseDto(
                rootNode.path("name").asText(),
                rootNode.path("sys").path("country").asText(),
                rootNode.path("main").path("temp").asDouble(),
                rootNode.path("weather").get(0).path("description").asText(),
                rootNode.path("main").path("humidity").asDouble()
        );
    }


}
