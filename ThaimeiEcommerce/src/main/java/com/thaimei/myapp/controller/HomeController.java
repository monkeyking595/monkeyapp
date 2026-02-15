package com.thaimei.myapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "user/index";
    }
    
    @GetMapping("/shopping_cart")
    public String shopping_cart() {
        return "user/shopping_cart";
    }
    
    @GetMapping("/products")
    public String products() {
        return "user/products";
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
        return "error/success";
    }
    @GetMapping("/products_detail")
    public String products_detail() {
        return "user/products_detail";
    }
    @GetMapping("/error")
    public String error() {
        return "usererror";
    }
    @GetMapping("/register") 
    public String adminregistration() {
        return "admin/admin-registration";
    }
    @GetMapping("/login")
    public String login_page() {
        return "user/login_page";
    }
      @GetMapping("/profile")
    public String profile() {
    return "user/UserProfile";
    }
     @GetMapping("/signup")
    public String signup() {
        return "user/signup";
    }
    @GetMapping("/OrderPage")
    public String getOrderPage() {
        return "user/userorder";
    }
    @GetMapping("/ProductList")
    public String getProductList() {
        return "user/product_lists";
    }
    @GetMapping("/ProductDetails")
    public String getProductDetails() {
        return "products_details";
    }
    

    }
    
    
    
    

    
        

    


