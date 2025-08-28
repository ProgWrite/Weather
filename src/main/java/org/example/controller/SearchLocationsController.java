package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SearchLocationsController {

    @GetMapping("/search")
    public String showSearchPage(Model model) {
        return "search";
    }

}
