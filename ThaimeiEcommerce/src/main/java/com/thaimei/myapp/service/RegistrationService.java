package com.thaimei.myapp.service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.dto.UserRegistrationDto;
import com.thaimei.myapp.dto.adminDto.AdminRegistrationDto;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public RegistrationService(UserService userService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder=passwordEncoder;
    }
    public void RegisterUser(UserRegistrationDto dto) {
        if (dto.getUsername()==null || dto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if(!dto.getPassword().equals(dto.getConfirmpassword())) {
            throw new IllegalArgumentException("password does not match!");
        }
        if (userService.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }

    
    User user=new User();
    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setEmail(dto.getEmail());
    user.setRole("USER");
    userService.save(user);
    }

    public void adminRegister(AdminRegistrationDto adDto) {
        if (adDto.getAdminname()==null || adDto.getAdminname().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if(!adDto.getAdminpassword().equals(adDto.getAdminconfirmpassword())) {
            throw new IllegalArgumentException("password does not match!");
        }
        if (userService.existsByUsername(adDto.getAdminname())) {
            throw new IllegalArgumentException("Username already exists!");

    }
    User user=new User();

    user.setUsername(adDto.getAdminname());
    user.setPassword(passwordEncoder.encode(adDto.getAdminpassword()));
    user.setRole("ADMIN");
    user.setEmail(adDto.getAdminemail());
    userService.save(user);
    }
    
}
