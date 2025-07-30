package org.example.controller;

//TODO удали этот класс
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckerController {

    @GetMapping("/checker")
    public String checker() {
        return "checker";
    }
}
