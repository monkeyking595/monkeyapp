package com.thaimei.myapp.controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;


@Controller

public class PaymentController {
    @GetMapping("/payment")
    public String payment() {
        return "payment";
    }
    
    
}
