package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.TestFileUtils;
import org.example.config.TestAppConfig;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.dto.WeatherResponseDto;
import org.example.exceptions.LocationExistsException;
import org.example.exceptions.LocationNotFoundException;
import org.example.mapper.LocationMapper;
import org.example.model.Location;
import org.example.model.User;
import org.example.repository.LocationRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = TestAppConfig.class)
@Transactional
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestPropertySource(properties = "openWeatherApiKey=test_dummy_key_12345")
@ExtendWith(MockitoExtension.class)
public class LocationServiceIT {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(locationService, "httpClient", httpClient);
    }

    @Test
    public void shouldFindLocation() throws IOException, InterruptedException {
        String mockJsonResponse = TestFileUtils.readJsonResponse("response.json");

        when(httpResponse.body()).thenReturn(mockJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        List<LocationResponseDto> result = locationService.findLocations("Moscow");

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Moscow", result.get(0).name());
        assertEquals("RU", result.get(0).country());
        assertEquals("Moscow", result.get(1).name());
        assertEquals("US", result.get(1).country());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any());

    }

    @Test
    public void shouldThrowLocationNotFoundException() throws IOException, InterruptedException {
        String mockJsonResponse = "[]";

        when(httpResponse.body()).thenReturn(mockJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        assertThrows(LocationNotFoundException.class, () -> {
            locationService.findLocations("Moscow");
        });

    }

    @Test
    public void shouldAddLocation() {
        List<Location> locations = new ArrayList<>();
        LocationResponseDto locationResponseDto = new LocationResponseDto("Moscow", 55.75, 37.61, "RU", "RUS");
        UserResponseDto userDto = new UserResponseDto(1L, "Dimka");
        User user = new User(1L, "log", "pass");

        when(locationRepository.findAllByUserId(userDto.getId())).thenReturn(locations);
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));
        locationService.addLocation(locationResponseDto, userDto);

        verify(userRepository).findByLogin("Dimka");
        verify(locationRepository).findAllByUserId(1L);
        verify(locationRepository).save(any(Location.class));

    }

    @Test
    public void shouldThrowLocationExistsException() {
        List<LocationResponseDto> locationsDto = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        LocationResponseDto locationResponseDto = new LocationResponseDto("Moscow", 55.75, 37.61, "RU", "RUS");
        locationsDto.add(locationResponseDto);
        for (LocationResponseDto locationDto : locationsDto) {
            Location location = LocationMapper.INSTANCE.toEntity(locationDto);
            locations.add(location);
        }
        UserResponseDto userDto = new UserResponseDto(1L, "Dimka");
        User user = new User(1L, "log", "pass");

        when(locationRepository.findAllByUserId(userDto.getId())).thenReturn(locations);
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));

        LocationExistsException exception = assertThrows(LocationExistsException.class, () -> {
            locationService.addLocation(locationResponseDto, userDto);
        });

        assertEquals("Location already exists with name Moscow", exception.getMessage());
        verify(userRepository).findByLogin("Dimka");
        verify(locationRepository).findAllByUserId(1L);
        verify(locationRepository, never()).save(any(Location.class));
    }


    @Test
    public void shouldDeleteLocation() {
        WeatherResponseDto weather = buildTestWeatherResponseDto();
        UserResponseDto userDto = new UserResponseDto(1L, "Dimka");
        User user = new User(1L, "log", "pass");
        Location locationToDelete = buildTestLocation(user);

        when(locationRepository.findAllByUserId(1L)).thenReturn(List.of(locationToDelete));

        locationService.delete(weather, userDto);
        verify(locationRepository).deleteByCoordinates(55.75, 37.61);
        verify(locationRepository).findAllByUserId(1L);
    }

    private WeatherResponseDto buildTestWeatherResponseDto() {
        WeatherResponseDto weather = new WeatherResponseDto("Moscow", 55.75, 37.61, "RU", 18.0, "Sunny", 75.0, 20.0, "__");
        return weather;
    }

    private Location buildTestLocation(User user) {
        Location location = new Location();
        location.setId(1L);
        location.setName("Moscow");
        location.setUser(user);
        location.setLatitude(BigDecimal.valueOf(55.75));
        location.setLongitude(BigDecimal.valueOf(37.61));

        return location;
    }


}
