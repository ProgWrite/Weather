package org.example.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequestDto;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class RegistrationController {

    private final UserService userService;

    @GetMapping()
    public String signUp(Model model) {
        model.addAttribute("user", new UserRequestDto());
        return "sign-up";
    }

    @PostMapping
    public String createUser(@ModelAttribute @Valid UserRequestDto user){
        userService.create(user);
        return "redirect:/";
    }

}
