package com.thaimei.myapp.security;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  UserDetailsServiceImpl userDetailsServiceImpl;
    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final  String  authHeader = request.getHeader("Authorization");
        String username= null;
        String token= null;
         if(authHeader != null && authHeader.startsWith("Bearer")) {
            token=authHeader.substring(7);
            try {
                username= jwtUtil.extractSubject(token);
                if(jwtUtil.isTokenexpired(token)) {
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                 return;
                }
            }
            catch(Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                var userDetails=userDetailsServiceImpl.loadUserByUsername(username);
                 if(userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            }
           
            
         }
          filterChain.doFilter(request, response);

    }
    
    
}
