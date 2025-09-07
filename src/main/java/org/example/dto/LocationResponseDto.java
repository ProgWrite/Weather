package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationResponseDto(String name, double lat, double lon, String country, String state) {

}
