package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LocationResponseDto;
import org.example.model.Location;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j



public class LocationService {

    private final static String API_KEY = "b6342efa2a5bf5746f4eb7015b4bd14b";
    private final JsonMapper jsonMapper = new JsonMapper();

    //TODO будет валидация + исключения
    public List<LocationResponseDto> findLocations(String locationName) throws IOException, InterruptedException {

        String url = buildUrl(locationName);
        HttpRequest request = buildHttpRequest(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        String jsonResponse = response.body();

        List<LocationResponseDto> locations = jsonMapper.readValue(jsonResponse, new TypeReference<List<LocationResponseDto>>() {});
        return locations;
    }


    private String buildUrl(String locationName) {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + locationName + "&limit=5" + "&appid=" + API_KEY;
        return url;
    }

    private HttpRequest buildHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }





}
