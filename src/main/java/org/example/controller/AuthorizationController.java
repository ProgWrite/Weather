package org.example.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserAuthorizationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exceptions.WrongPasswordException;
import org.example.model.Session;
import org.example.service.SessionService;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RequestMapping("sign-in")
@RequiredArgsConstructor
@Controller
public class AuthorizationController {

    private final UserService userService;
    private final SessionService sessionService;

    @GetMapping()
    public String showAuthorizationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserAuthorizationRequestDto());
        }
        return "sign-in";
    }

    @PostMapping
    public String authorizeUser(@ModelAttribute @Valid UserAuthorizationRequestDto user,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                HttpServletResponse response
    ) {

        redirectAttributes.addFlashAttribute("user", user);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/sign-in";
        }

        try {
            Optional<UserResponseDto> userResponseDto = userService.getUser(user);

            Session session = sessionService.create(user);
            String sessionId = session.getId().toString();

            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            redirectAttributes.addFlashAttribute("user", userResponseDto);

            return "redirect:/";

        } catch (WrongPasswordException exception) {
            redirectAttributes.addFlashAttribute("authorizeError", exception.getMessage());
            return "redirect:/sign-in";
        }

    }

}
