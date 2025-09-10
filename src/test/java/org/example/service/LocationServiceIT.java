package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.config.TestAppConfig;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.LocationNotFoundException;
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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        String mockJsonResponse = "[{\"name\":\"Moscow\",\"lat\":55.7558,\"lon\":37.6173}]";

        when(httpResponse.body()).thenReturn(mockJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        List<LocationResponseDto> result = locationService.findLocations("Moscow");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Moscow", result.get(0).name());
        verify(httpClient, times(1)).send(any(HttpRequest.class), any());

    }

    @Test
    public void shouldThrowLocationNotFoundException() throws IOException, InterruptedException {
        String mockJsonResponse = "[]";

        when(httpResponse.body()).thenReturn(mockJsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        assertThrows(LocationNotFoundException.class, () -> {
            locationService.findLocations("Moscow");
        });

    }

    @Test
    public void shouldAddLocation() throws IOException, InterruptedException {
        UserResponseDto userDto =  new UserResponseDto(1L,"Dimka");
        LocationResponseDto locationResponseDto =
                new LocationResponseDto("Moscow", 55.75, 37.61, "RU", "RUS");
        User user = new User(1L, "log", "pass");

        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));

        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> {
            Location savedLocation = invocation.getArgument(0);
            savedLocation.setId(1L);
            return savedLocation;
        });

        locationService.addLocation(locationResponseDto, userDto);

        List<LocationResponseDto> locations = locationService.getAllLocations(userDto);

       assertNotNull(locations);
       assertEquals(1, locations.size());
       assertEquals("Moscow", locations.get(0).name());

    }

}
