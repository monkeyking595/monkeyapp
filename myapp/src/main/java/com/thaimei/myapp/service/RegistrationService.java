package com.thaimei.myapp.service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.dto.UserRegistrationDto;
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

    
    User user=new User(
        dto.getUsername(),
        passwordEncoder.encode(dto.getPassword()),
        dto.getEmail()
    );
    userService.save(user);
    }
    
}
