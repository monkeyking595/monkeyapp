package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import com.thaimei.myapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.AuthenticationException;
import com.thaimei.myapp.dto.JwtResponse;
import com.thaimei.myapp.dto.adminDto.AdminLoginDto;
import com.thaimei.myapp.error.AppException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.thaimei.myapp.security.CustomUserDetails;



@RestController
@RequestMapping("/admin/api")
public class AdminLoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AdminLoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/adminlogin")
    public ResponseEntity<JwtResponse> adlogin(@Valid @RequestBody AdminLoginDto adminLoginDto) {
         Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(adminLoginDto.getAdminUsername(), adminLoginDto.getAdminPassword())
            );
        }

        //catch the AuthenticationException before it reaches the JWTfilter, this is needed if you want a clearer message instead of the default exception thrown by the filter.
        catch(AuthenticationException e ) {
            throw new AppException("Invalid username or password",401);
        }

        boolean isAdmin = authentication.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if(!isAdmin) {
            throw new AppException("Acess denied",  403);
        }
        
         
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String token=jwtUtil.generateToken(String.valueOf(userId), 3600000L);
        return ResponseEntity.ok(new JwtResponse(token, authentication.getName())
        );
    }
}



