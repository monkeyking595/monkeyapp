package com.thaimei.myapp.controller;
import com.thaimei.myapp.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thaimei.myapp.dto.JwtResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;



@Controller
@RequestMapping
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @GetMapping("/login_page")
    public String login_page() {
        return "login_page";
    }

    @PostMapping("/login")
    @ResponseBody
    ResponseEntity<JwtResponse> login (@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtUtil.generateToken(authentication.getName(), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, authentication.getName()));

    } 

    
    }
    
  
    

