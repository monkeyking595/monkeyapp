package com.thaimei.myapp.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    @GetMapping("/login_page")
    public String login_page() {
        return "login_page";
    }
    
}
