package com.thaimei.myapp.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import com.thaimei.myapp.service.RegistrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.thaimei.myapp.dto.UserRegistrationDto;
import jakarta.validation.Valid; 



@Controller

public class RegistrationController {
    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    @GetMapping("/your_account")
    public String your_account(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "your_account";
    }
    
    @PostMapping("/your_account")
    public String updateAccount(
        @Valid @ModelAttribute("userRegistrationDto") UserRegistrationDto dto,
        BindingResult result,
        Model model) {
            if (result.hasErrors()) {
                return "your_account";
            }
            try {
                registrationService.RegisterUser(dto);
                model.addAttribute("message", "account created successfully!");
            } catch (Exception e) {
                model.addAttribute("error",e.getMessage());
            }
            return "your_account";
        
        }
       
    }
        