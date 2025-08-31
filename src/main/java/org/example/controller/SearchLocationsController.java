package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.LocationResponseDto;
import org.example.service.LocationService;
import org.example.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchLocationsController {

    private final LocationService locationService;
    private final WeatherService weatherService;

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        return "search";
    }

    // TODO подумать о нейминге. Здесь я добавляю локацию и ищу погоду и отдаю это на сервер.
    //  Поменять местами. Сначала будет добавлять локацию в БД, затем искать погоду
    @PostMapping public String addLocation(LocationResponseDto locationResponseDto) throws IOException, InterruptedException {
        weatherService.findWeather(locationResponseDto);
        locationService.addLocation(locationResponseDto);

        return "/";
    }

}
