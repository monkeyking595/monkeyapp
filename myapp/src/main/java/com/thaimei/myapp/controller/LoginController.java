package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @GetMapping("/login_page")
    public String login_page() {
        return "login_page";
    }
    @PostMapping("/login_page")
    public String Login_page() {
        return "login_page";
    }
       
    
}
