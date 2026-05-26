package com.thaimei.myapp.controller.sellers;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.sellersDto.SellersLoginDto;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.thaimei.myapp.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import com.thaimei.myapp.security.JwtUtil;
import com.thaimei.myapp.dto.JwtResponse;


@RequestMapping("/sellers")
@RestController
public class sellerLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public sellerLoginController (AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil  = jwtUtil;
    }

    
    @PostMapping("/sellerLogin")
    public ResponseEntity<JwtResponse> sellerLoginPost(@Valid @RequestBody SellersLoginDto sellersLoginDto ) {
        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(sellersLoginDto.getSellersName(),
         sellersLoginDto.getSellersPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String token = jwtUtil.generateToken(String.valueOf(userId), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, sellersLoginDto.getSellersName()));

    }
    
}
