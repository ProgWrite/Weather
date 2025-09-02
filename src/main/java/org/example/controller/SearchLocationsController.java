package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.dto.LocationResponseDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.LocationExistsException;
import org.example.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchLocationsController {

    private final LocationService locationService;

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }


    @PostMapping
    public String addLocation(HttpServletRequest request,
                              LocationResponseDto locationResponseDto,
                              RedirectAttributes redirectAttributes) {
        try{
            UserResponseDto user = (UserResponseDto) request.getAttribute("user");
            locationService.addLocation(locationResponseDto, user);
            return "redirect:/";
        }catch(LocationExistsException exception){
            redirectAttributes.addFlashAttribute("locationExist", exception.getMessage());
            return "redirect:/";
        }
    }

}
