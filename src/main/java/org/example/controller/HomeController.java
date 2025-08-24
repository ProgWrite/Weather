package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.UserResponseDto;
import org.example.mapper.UserMapper;
import org.example.service.SessionService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping("/")
    public String showHomePage(
            @CookieValue(value = "sessionId", required = false) String sessionId,
            Model model) {

        Optional<UserResponseDto> user = userService.getUserBySession(sessionId);
        boolean isLoggedIn = user.isPresent();

        if (!isLoggedIn && sessionId != null) {
            //TODO нейминг метода
            sessionService.deleteExpiredSessions(sessionId);
        }

        model.addAttribute("isLoggedIn", isLoggedIn);
        user.ifPresent(u -> model.addAttribute("user", u));
        return "index";
    }
}

