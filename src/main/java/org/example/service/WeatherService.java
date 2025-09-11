package org.example.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LocationResponseDto;
import org.example.dto.WeatherResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    @Value("${openWeatherApiKey}")
    private String API_KEY;
    private final JsonMapper jsonMapper = new JsonMapper();

    public List<WeatherResponseDto> findWeather(List<LocationResponseDto> locations) throws IOException, InterruptedException {
        List<WeatherResponseDto> weathers = new ArrayList<>();

        for (LocationResponseDto location : locations) {
            WeatherResponseDto weather = buildWeather(location);
            weathers.add(weather);
        }
        return weathers;
    }

    private WeatherResponseDto buildWeather(LocationResponseDto location) throws IOException, InterruptedException {
        String url = buildUrl(location);
        HttpRequest request = buildHttpRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();
        JsonNode rootNode = jsonMapper.readTree(jsonResponse);
        WeatherResponseDto weather = WeatherResponseDto.fromJson(rootNode, location);
        return weather;
    }

    private String buildUrl(LocationResponseDto locationResponseDto) {
        String latitude = String.valueOf(locationResponseDto.lat());
        String longitude = String.valueOf(locationResponseDto.lon());
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                latitude, longitude, API_KEY);
        return url;
    }

    private HttpRequest buildHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }

}
