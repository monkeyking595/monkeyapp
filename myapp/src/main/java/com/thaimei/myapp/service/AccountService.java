package com.thaimei.myapp.service;
import com.thaimei.myapp.model.User;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserService userService;
    public AccountService(UserService userService) {
        this.userService = userService;
    }
    public void registerUser(String username, String password, String confirmpassword, String email) {
        if (username==null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if(!password.equals(confirmpassword)) {
            throw new IllegalArgumentException("password does not match!");
        }
        if (userService.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists!");
        }

    
    User user=new User(username, password, email);
    userService.save(user);
    }
    
}
