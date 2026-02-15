package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.adminDto.AdminUserDto;
import com.thaimei.myapp.model.User;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.stereotype.Service;

import com.thaimei.myapp.repository.UserRepository;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, PasswordEncoder passwordencoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordencoder;
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public User findById(Long id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
    }
    public List<AdminUserDto> getAllUsersForAdmin() {
       List<User> users = userRepository.findAll();
       return users.stream()
       .map(user-> new AdminUserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole(),
        user.getStatus()
       ))
       .toList();
        
    }

    
    
}
