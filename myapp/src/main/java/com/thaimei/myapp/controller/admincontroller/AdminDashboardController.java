package com.thaimei.myapp.controller.admincontroller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/admin")
@Controller

public class AdminDashboardController {
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

}