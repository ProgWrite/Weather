package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.LocationService;
import org.example.service.UserService;
import org.example.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchLocationsController {

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        return "search";
    }

    // TODO подумать о нейминге. Здесь я добавляю локацию и ищу погоду и отдаю это на сервер.
    //  Поменять местами. Сначала будет добавлять локацию в БД, затем искать погоду
    @PostMapping
    public String addLocation(HttpServletRequest request, LocationResponseDto locationResponseDto) throws IOException, InterruptedException {
        UserResponseDto user = (UserResponseDto) request.getAttribute("user");
        locationService.addLocation(locationResponseDto, user);
        weatherService.findWeather(locationResponseDto);
        return "/";
    }

}
