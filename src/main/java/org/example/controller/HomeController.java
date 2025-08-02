package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);
        return "index";
    }
}
