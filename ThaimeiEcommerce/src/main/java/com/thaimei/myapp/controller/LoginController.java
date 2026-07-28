package com.thaimei.myapp.controller;
import com.thaimei.myapp.dto.LoginRequest;
import com.thaimei.myapp.error.AppException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.dto.JwtResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.repository.UserRepository;
import com.thaimei.myapp.security.CustomUserDetails;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/customers")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login (@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            ) 
        );
        } catch(AuthenticationException e) {
            throw new AppException("Invalid username or password",  401);
        }
        //getAuthorities(), a method of authentication, which is built using the data from the CustomUserDetails(), it holds a lit of simpleGrantedAuthorities 
        // getauthority(), this is method of GrantedAuthorities which is implemented by simpleGrantedAuthorities, it returns the actual String (ROLE), it unwraps the role from the object.
        boolean isCustomer = authentication.getAuthorities().stream()
        .anyMatch(a-> a.getAuthority().equals("ROLE_CUSTOMER"));

        if(!isCustomer) {
            throw new AppException("Access Denied", 403);
        }
        //casting of return type 
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId=userDetails.getId();
        String token=jwtUtil.generateToken(String.valueOf(userId), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, authentication.getName()));
    }  

    
    }
    
  
    

