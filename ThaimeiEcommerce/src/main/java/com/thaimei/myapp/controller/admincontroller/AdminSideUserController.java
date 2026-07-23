package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.service.UserService;

import jakarta.validation.Valid;

import  com.thaimei.myapp.dto.adminDto.AdminUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import com.thaimei.myapp.enums.RoleEnum;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import com.thaimei.myapp.dto.adminDto.UpdateUserStatus;
@RequestMapping("/admin/api")
@RestController
public class AdminSideUserController {
    private final UserService userService;
    public AdminSideUserController(UserService userService) {
        this.userService = userService;
    }

    // this get will be used for both the customer and seller (generic), frontend sends the role.
    @GetMapping("/customers/sellers")
    public ResponseEntity<Slice<AdminUserDto>> getAllCustomer(@RequestParam (defaultValue ="0")int page, @RequestParam(defaultValue="20")int size, @RequestParam RoleEnum role) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<AdminUserDto> users = userService.getUserByRole(role, pageable);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/searchSellers")
    public ResponseEntity<AdminUserDto> searchSeller(@RequestParam String email) {
        AdminUserDto seller = userService.searchByEmail(email);
        return ResponseEntity.ok(seller);
    }

    @PatchMapping("/updateUserStatus/{userId}")
    public ResponseEntity<?> disableUser (@PathVariable Long userId,@Valid @RequestBody UpdateUserStatus updateUserStatus) {
        userService.updateUserStatus(userId, updateUserStatus);
        return ResponseEntity.ok(Map.of("message","user status updated successfully!"));
    }
    
}
