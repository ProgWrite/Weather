package org.example.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final UserService userService;

    @PostMapping("/logout")
    public String logout(@CookieValue("sessionId") String sessionId,
                         HttpServletResponse response, HttpServletRequest request) {

        userService.logout(sessionId);
        Cookie cookie = new Cookie("sessionId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
