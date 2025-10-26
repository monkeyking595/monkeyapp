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
import com.thaimei.myapp.dto.adminDto.AdminRegistrationDto;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import jakarta.validation.Valid; 




@RestController
@RequestMapping
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
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationDto adDto) {
        try {
            registrationService.adminRegister(adDto);
            Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adDto.getAdminname(),adDto.getAdminpassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token=jwtUtil.generateToken(adDto.getAdminname(),  3600000L);
            return ResponseEntity.ok(new JwtResponse(token, adDto.getAdminname()));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }

    }
    
}
