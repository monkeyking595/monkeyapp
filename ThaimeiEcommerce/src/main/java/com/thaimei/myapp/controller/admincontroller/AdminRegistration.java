package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import com.thaimei.myapp.service.RegistrationService;
import com.thaimei.myapp.dto.JwtResponse;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import jakarta.validation.Valid; 
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.dto.UserRegistrationDto;
import com.thaimei.myapp.model.RoleEnum;




@RestController
@RequestMapping("/admin/api")
public class AdminRegistration {
    private final RegistrationService registrationService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    public AdminRegistration(RegistrationService registrationService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.registrationService= registrationService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserRegistrationDto adDto) {
        try {
            registrationService.RegisterUser(adDto, RoleEnum.ADMIN);
            Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adDto.getUsername(),adDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            String token=jwtUtil.generateToken(String.valueOf(userId),  3600000L);
            return ResponseEntity.ok(new JwtResponse(token, adDto.getUsername()));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

    }
    
}
