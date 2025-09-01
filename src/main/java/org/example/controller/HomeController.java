package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.dto.WeatherResponseDto;
import org.example.service.LocationService;
import org.example.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LocationService locationService;
    private final WeatherService weatherService;

    @GetMapping("/")
    public String showHomePage(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        UserResponseDto user = (UserResponseDto) request.getAttribute("user");
        if (user != null) {
            List<LocationResponseDto> locations = locationService.getAllLocations(user);
            List<WeatherResponseDto> weathers = weatherService.findWeather(locations);
            model.addAttribute("weathers", weathers);
        }
        return "index";
    }

//TODO тут будет Dtoшка и буду использовать ModelAttribute и возможно валидацию (как в других контролерах).Т
// Также будет LocationResponseDto с валидацией
    @PostMapping
    public String searchLocations(HttpServletRequest request, Model model) throws IOException, InterruptedException {
        String locationName = request.getParameter("location");
        List<LocationResponseDto> locations = locationService.findLocations(locationName);
        model.addAttribute("locations", locations);
        return "search";
    }

}
