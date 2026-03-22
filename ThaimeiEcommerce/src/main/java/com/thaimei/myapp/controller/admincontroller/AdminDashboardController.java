package com.thaimei.myapp.controller.admincontroller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/admin")
@Controller

public class AdminDashboardController {
    @GetMapping("/")
    public String adminDashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/registration") 
    public String adminregistration() {
        return "admin/adminRegistration";
    }
    @GetMapping("/adminProducts")
    public String adminProducts() {
        return "admin/adminProducts";
    }
    @GetMapping("/adminOrders")
    public String adminOrders() {
        return "admin/adminOrders";
    }
    @GetMapping("/adminUsers")
    public String adminUsers() {
        return "admin/adminUsers";
    }


}