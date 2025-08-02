package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LogoutController {

    private final UserService userService;

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/sign-in";
    }


//    @PostMapping("/logout")
//    public String logout(@CookieValue("SESSION_ID") String sessionId,
//                         HttpServletResponse response) {
//        userService.logout(sessionId);
//
//        // Очищаем куку
//        Cookie cookie = new Cookie("SESSION_ID", "");
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return "redirect:/sign-in";
//    }

}
