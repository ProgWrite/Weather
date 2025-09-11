package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationRequestDto;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.dto.WeatherResponseDto;
import org.example.exceptions.LocationExistsException;
import org.example.exceptions.LocationNotFoundException;
import org.example.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class LocationsController {

    private final LocationService locationService;

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    @PostMapping
    public String addLocation(HttpServletRequest request,
                              LocationResponseDto locationResponseDto,
                              RedirectAttributes redirectAttributes) {
        try {
            UserResponseDto user = (UserResponseDto) request.getAttribute("user");
            locationService.addLocation(locationResponseDto, user);
            return "redirect:/";
        } catch (LocationExistsException exception) {
            redirectAttributes.addFlashAttribute("locationExist", exception.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/find")
    public String searchLocations(@ModelAttribute @Valid LocationRequestDto location,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes)
            throws IOException, InterruptedException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("location", location);
            return "redirect:/";
        }

        try {
            String locationName = location.getName();
            List<LocationResponseDto> locations = locationService.findLocations(locationName);
            model.addAttribute("locations", locations);
            return "search";
        } catch (LocationNotFoundException exception) {
            redirectAttributes.addFlashAttribute("locationNotFound", exception.getMessage());
            return "redirect:/";
        }

    }

    @PostMapping("/delete")
    public String deleteLocation(WeatherResponseDto weather,
                                 HttpServletRequest request) {
        UserResponseDto user = (UserResponseDto) request.getAttribute("user");
        locationService.delete(weather, user);
        return "redirect:/";
    }

}
