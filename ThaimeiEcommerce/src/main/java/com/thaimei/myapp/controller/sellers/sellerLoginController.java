package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.sellersDto.SellersLoginDto;
import com.thaimei.myapp.error.AppException;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.security.JwtUtil;
import com.thaimei.myapp.dto.JwtResponse;


@RequestMapping("/sellers")
@RestController
public class SellerLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public SellerLoginController (AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil  = jwtUtil;
    }

    
    @PostMapping("/sellerLogin")
    public ResponseEntity<JwtResponse> sellerLoginPost(@Valid @RequestBody SellersLoginDto sellersLoginDto ) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(sellersLoginDto.getSellersName(),
         sellersLoginDto.getSellersPassword()));
        }
        catch (AuthenticationException e) {
            throw new AppException("Invalid username or password", 401);
        }

        boolean iSeller = authentication.getAuthorities().stream()
        .anyMatch(s -> s.getAuthority().equals("ROLE_SELLER"));

        if(!iSeller) {
            throw new AppException("Acess denied", 403);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String token = jwtUtil.generateToken(String.valueOf(userId), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, sellersLoginDto.getSellersName()));
    }
    
}
