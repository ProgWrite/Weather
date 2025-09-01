package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.UserNotFoundException;
import org.example.mapper.LocationMapper;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final static String API_KEY = "b6342efa2a5bf5746f4eb7015b4bd14b";
    private final JsonMapper jsonMapper = new JsonMapper();
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

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

    public void addLocation(LocationResponseDto locationResponseDto, UserResponseDto userDto){
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        Location location = LocationMapper.INSTANCE.toEntityWithUser(locationResponseDto, user);
        locationRepository.save(location);
    }

    public List<LocationResponseDto> getAllLocations(UserResponseDto user){
        Long userId = user.getId();
        List<Location> locations = locationRepository.findAllByUserId(userId);

        List<LocationResponseDto> locationResponseDtos =
                locations.stream()
                 .map(LocationMapper.INSTANCE::toDto)
                 .collect(Collectors.toList());

       return locationResponseDtos;
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
