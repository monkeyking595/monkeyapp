package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.UserRegistrationDto;
import com.thaimei.myapp.dto.JwtResponse;
import com.thaimei.myapp.service.RegistrationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.thaimei.myapp.security.JwtUtil;



import jakarta.validation.Valid;

@RestController
@RequestMapping("/seller")
public class SellerRegistration {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public SellerRegistration(RegistrationService registrationService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
   

    
    @PostMapping("/registration")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody UserRegistrationDto  sellerRegDto) {
        try {
            registrationService.registerSeller(sellerRegDto);
            Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(sellerRegDto.getUsername(), sellerRegDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();
            String token = jwtUtil.generateToken(String.valueOf(userId), 3600000L);
            return ResponseEntity.ok(new JwtResponse(token, sellerRegDto.getUsername()));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
        
