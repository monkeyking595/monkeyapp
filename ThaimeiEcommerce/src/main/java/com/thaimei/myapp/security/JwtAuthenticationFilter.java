package com.thaimei.myapp.security;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;


import org.springframework.lang.NonNull;

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
    protected void doFilterInternal(@NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        final  String  authHeader = request.getHeader("Authorization");
        String path=request.getServletPath();
        if(path.equals("/")||path.equals("/signup")||path.equals("/login_page")||path.equals("/login")||path.equals("/error")||path.startsWith("/css/")||path.startsWith("/myimages/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String userIdToken= null;
        String token= null;
         if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token=authHeader.substring(7);
            try {
                  userIdToken = jwtUtil.extractSubject(token);
                if(jwtUtil.isTokenExpired(token)) {
                    throw new BadCredentialsException("token expired", new RuntimeException("token expired"));
            }
        }
            catch(Exception e) {
                throw new InsufficientAuthenticationException("invalid token",e);
            }
            if(userIdToken!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
                Long userId= Long.valueOf(userIdToken);
                var userDetails=userDetailsServiceImpl.loadUserById(userId);
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
