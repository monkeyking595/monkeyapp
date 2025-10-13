package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.RegistrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.UserRegistrationDto;
import jakarta.validation.Valid; 
import java.util.Map;
import com.thaimei.myapp.dto.JwtResponse;
import com.thaimei.myapp.security.JwtUtil;



@RestController
@RequestMapping
public class RegistrationController {
    private final RegistrationService registrationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    public RegistrationController(RegistrationService registrationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        
    }
   
    
    @PostMapping("/signup")
    public ResponseEntity<?> registration(@Valid @RequestBody UserRegistrationDto dto ) {
        try {
            registrationService.RegisterUser(dto);
             Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
             );
             SecurityContextHolder.getContext().setAuthentication(authentication);

            String token=jwtUtil.generateToken(dto.getUsername(), 3600000L);

        return ResponseEntity.ok(new JwtResponse(token, dto.getUsername()));

        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
       
        
    }

            
        
        
       
    }
   