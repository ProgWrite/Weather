package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.dto.WeatherResponseDto;
import org.example.exceptions.LocationExistsException;
import org.example.exceptions.LocationNotFoundException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.WeatherNotFoundException;
import org.example.mapper.LocationMapper;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    @Value("${openWeatherApiKey}")
    private String API_KEY;
    private final JsonMapper jsonMapper = new JsonMapper();
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<LocationResponseDto> findLocations(String locationName) throws IOException, InterruptedException {
        String url = buildUrl(locationName);
        HttpRequest request = buildHttpRequest(url);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();
        List<LocationResponseDto> locations = jsonMapper.readValue(jsonResponse, new TypeReference<List<LocationResponseDto>>() {
        });
        if (locations.isEmpty()) {
            throw new LocationNotFoundException("The location you entered not found. Please try again");
        }
        return locations;
    }

    public void addLocation(LocationResponseDto locationResponseDto, UserResponseDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (isLocationExists(locationResponseDto, userDto)) {
            throw new LocationExistsException("Location already exists with name " + locationResponseDto.name());
        }

        Location location = LocationMapper.INSTANCE.toEntityWithUser(locationResponseDto, user);
        locationRepository.save(location);
    }

    public List<LocationResponseDto> getAllLocations(UserResponseDto user) {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Long userId = user.getId();
        List<Location> locations = locationRepository.findAllByUserId(userId);

        List<LocationResponseDto> locationResponseDtos =
                locations.stream()
                        .map(LocationMapper.INSTANCE::toDto)
                        .collect(Collectors.toList());
        return locationResponseDtos;
    }

    public void delete(WeatherResponseDto weather, UserResponseDto user) {
        if (weather == null) {
            throw new WeatherNotFoundException("Weather not found");
        }

        List<LocationResponseDto> locations = getAllLocations(user);
        for (LocationResponseDto location : locations) {
            if (location.lat() == weather.lat() && location.lon() == weather.lon()) {
                locationRepository.deleteByCoordinates(weather.lat(), weather.lon());
            }
        }
    }

    private boolean isLocationExists(LocationResponseDto location, UserResponseDto user) {
        double latitude = location.lat();
        double longitude = location.lon();

        double roundedLatitude = Math.round(latitude * 100.0) / 100.0;
        double roundedLongitude = Math.round(longitude * 100.0) / 100.0;


        List<LocationResponseDto> locations = getAllLocations(user);
        for (LocationResponseDto locationDto : locations) {
            if (locationDto.lat() == roundedLatitude && locationDto.lon() == roundedLongitude) {
                return true;
            }
        }
        return false;
    }

    private String buildUrl(String locationName) {
        String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
        String url = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s",
                encodedLocationName, API_KEY);
        return url;
    }

    private HttpRequest buildHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
    }

}
