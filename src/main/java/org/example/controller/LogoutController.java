package org.example.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.repository.SessionRepository;
import org.example.repository.UserRepository;
import org.example.service.SessionService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final SessionService sessionService;

    @PostMapping("/logout")
    public String logout(@CookieValue("sessionId") String sessionId,
                         HttpServletResponse response, HttpServletRequest request) {

        sessionService.logout(sessionId);
        Cookie cookie = new Cookie("sessionId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }
}
