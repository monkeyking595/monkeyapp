package com.thaimei.myapp.service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.dto.UserRegistrationDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.RoleEnum;


@Service
public class RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public RegistrationService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    public void RegisterUser(UserRegistrationDto dto, RoleEnum role) {
       
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
    user.setRole(role);
    userService.save(user);
    }
}
