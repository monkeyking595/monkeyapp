package com.thaimei.myapp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller

public class Account {
    @GetMapping("/your_account")
    public String your_account() {
        return "your_account";
    }
    
    @PostMapping("/your_account")
    public String updateAccount(
        @RequestParam String username,
        @RequestParam String password,
        @RequestParam(name="confirmpassword") String confirmpassword,
        @RequestParam(required=false) String email,
        Model model) {
            if(username.isBlank()|| password.isBlank()|| confirmpassword.isBlank()){
                model.addAttribute("error","username and password are required!");
                return "your_account";
            }
            if(!password.equals(confirmpassword)) {
                model.addAttribute("error", "password does not match!");
                return "your_account";
            }
            if(password.length()<8) {
                model.addAttribute("error", "password must be 8+ characters!");
                return "your_account";
            }
            model.addAttribute("message", "account created successfully!");
            return "your_account";
        }
        }

