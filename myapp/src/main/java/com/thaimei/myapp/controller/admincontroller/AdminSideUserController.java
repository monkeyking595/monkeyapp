package com.thaimei.myapp.controller.admincontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.service.UserService;
import  com.thaimei.myapp.dto.adminDto.AdminUserDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/admin")
@RestController
public class AdminSideUserController {
    private final UserService userService;
    public AdminSideUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/AllUsers")
    public ResponseEntity<List<AdminUserDto>> getAllUser() {
        List<AdminUserDto> users = userService.getAllUsersForAdmin();
        return ResponseEntity.ok(users);
    }
    
    
    
}
