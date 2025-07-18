package com.thaimei.myapp.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
@Controller

public class ProfileController {
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
    
}
