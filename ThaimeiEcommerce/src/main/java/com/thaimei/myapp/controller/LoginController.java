package com.thaimei.myapp.controller;
import com.thaimei.myapp.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.dto.JwtResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.UserRepository;



@RestController
@RequestMapping
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            ) 
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(authentication.getName())
        .orElseThrow(()-> new RuntimeException("User not found"));
        Long userId=user.getId();
        String token=jwtUtil.generateToken(String.valueOf(userId), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, authentication.getName()));

    }  

    
    }
    
  
    

