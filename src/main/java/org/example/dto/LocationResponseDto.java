package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//TODO может остальные классы сделаю рекордами
@JsonIgnoreProperties(ignoreUnknown = true)
public record LocationResponseDto(String name, double lat, double lon, String country, String state) {

}
