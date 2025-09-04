package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class LoginController {
    @GetMapping("/login_page")
    public String login_page() {
        return "login_page";
    }

    @PostMapping("/login_page")
    
    }
    
  
    

