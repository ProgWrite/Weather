package org.example.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("sign-in")
@Controller
public class AuthorizationController {

    @GetMapping()
    public String signIn(){
        return "sign-in";
    }

}
