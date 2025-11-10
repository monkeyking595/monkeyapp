package com.thaimei.myapp.security;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response,
    AuthenticationException authException) throws  IOException{
        response.setContentType("application/JSON");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String,Object> body=new HashMap<>();
        body.put("status",401);
        body.put("error","Unauthorized");
        body.put("message",authException.getMessage());
        body.put("path",request.getServletPath());
        new ObjectMapper().writeValue(response.getOutputStream(),body);

        
    }
    
}
