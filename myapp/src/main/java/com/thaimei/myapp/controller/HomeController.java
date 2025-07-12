package com.thaimei.myapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/your_account")
    public String your_account() {
        return "your_account";
    }
    @GetMapping("/shopping_cart")
    public String shopping_cart() {
        return "shopping_cart";
    }
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }
}
    
        

    


