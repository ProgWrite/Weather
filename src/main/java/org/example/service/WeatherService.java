package org.example.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LocationResponseDto;
import org.example.dto.WeatherResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final static String API_KEY = "b6342efa2a5bf5746f4eb7015b4bd14b";
    private final JsonMapper jsonMapper = new JsonMapper();

    //TODO метод будет возвращать WeatherResponseDto
    public void findWeather(LocationResponseDto locationResponseDto) throws IOException, InterruptedException {
        String url = buildUrl(locationResponseDto);
        HttpRequest request = buildHttpRequest(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String jsonResponse = response.body();

        //TODO ПОЧИТАЙ О rootNode, что это такое и как работает, разбери!!!
        JsonNode rootNode = jsonMapper.readTree(jsonResponse);
        WeatherResponseDto dto = WeatherResponseDto.fromJson(rootNode);


        //TODO возможно тут будет не лист, а одна погода. Подумать в дальнейшем
//        List<WeatherResponseDto> weathers = jsonMapper.readValue(jsonResponse, new TypeReference<List<WeatherResponseDto>>() {});

    }






    private String buildUrl(LocationResponseDto locationResponseDto)  {
        String latitude = String.valueOf(locationResponseDto.lat());
        String longitude = String.valueOf(locationResponseDto.lon());
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;
        return url;
    }

    private HttpRequest buildHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }


}
