package com.thaimei.myapp.controller;

import org.springframework.ui.Model;
import com.thaimei.myapp.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

@Controller

public class Account {
    private final AccountService accountService;
    public Account(AccountService accountService) {
        this.accountService = accountService;
    }
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
        try {
            accountService.registerUser(username,password,confirmpassword,email);
            model.addAttribute("message", "Account Created Successfully!");
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "your_account";
        
        }
    }

