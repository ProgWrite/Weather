package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationResponseDto;
import org.example.model.Location;
import org.example.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private final LocationService locationService;

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

//TODO тут будет Dtoшка и буду использовать ModelAttribute и возможно валидацию (как в других контролерах).Т
// Также будет LocationResponseDto с валидацией
    @PostMapping
    public String searchLocations(HttpServletRequest request) throws IOException, InterruptedException {
        String locationName = request.getParameter("location");
        List<LocationResponseDto> locations = locationService.findLocations(locationName);

        return "/";
    }






}
