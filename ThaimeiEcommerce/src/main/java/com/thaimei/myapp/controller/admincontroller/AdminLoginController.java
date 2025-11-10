package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import com.thaimei.myapp.dto.JwtResponse;
import com.thaimei.myapp.dto.adminDto.AdminLoginDto;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;



@RestController
@RequestMapping
public class AdminLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AdminLoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/adminlogin")
    public ResponseEntity<JwtResponse> adlogin(@Valid @RequestBody AdminLoginDto adminLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(adminLoginDto.getAdminUsername(), adminLoginDto.getAdminPassword())
            );
            String token=jwtUtil.generateToken(authentication.getName(), 3600000L);
            return ResponseEntity.ok(new JwtResponse(token, authentication.getName()));
    
    }
}

    

