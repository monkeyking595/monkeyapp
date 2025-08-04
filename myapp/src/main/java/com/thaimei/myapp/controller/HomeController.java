package com.thaimei.myapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/shopping_cart")
    public String shopping_cart() {
        return "shopping_cart";
    }
    
    @GetMapping("/products")
    public String products() {
        return "products";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/terms") 
    public String terms() {
        return "terms";
    }
    
    @GetMapping("/success")
    public String success() {
        return "success";
    }
    @GetMapping("products_detail")
    public String products_detail() {
        return "products_detail";
    }
    @GetMapping("/error")
    public String error() {
        return "error";
    }
    
    }
    

    
        

    


