package com.thaimei.myapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
    @PostMapping("/your_account")
    public String updateAccount(
        @RequestParam String username,
        @RequestParam String pw,
        @RequestParam(name="confirmpassword") String confirmpassword,
        @RequestParam(required=false) String email,
        Model model) {
            if(username.isBlank()|| pw.isBlank()|| confirmpassword.isBlank()){
                model.addAttribute("error","username and password are required!");
                return "your_account";
            }
            if(!pw.equals(confirmpassword)) {
                model.addAttribute("error", "password does not match!");
                return "your_account";
            }
            if(pw.length()<8) {
                model.addAttribute("error", "password must be 8+ characters!");
                return "your_account";
            }
            model.addAttribute("message", "account created successfully!");
            return "your_account";
        }
        }
        

    


