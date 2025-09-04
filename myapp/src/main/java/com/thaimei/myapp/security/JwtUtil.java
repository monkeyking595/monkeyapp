package com.thaimei.myapp.security;
import io.jsonwebtoken.*; 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key key;
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key= Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));  
    }
   public String generateToken(String subject, long expirationMillis) {
    Map<String, Object> claims= new HashMap<>();
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setExpiration(new Date(System.currentTimeMillis()+expirationMillis))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setIssuer("myapp")
        .setAudience("web-app")
        .signWith(key)
        .compact();

   }
   public Claims extractAllClaims(String token) throws JwtException {
    return Jwts.parser()
         .setSigningKey(key)
         .build()
         .parseClaimsJws(token)
         .getBody();                 
   }
   
    public<T> T extractClaims(String token, Function<Claims, T> ClaimResolver) {
        final Claims claims=extractAllClaims(token);
        return ClaimResolver.apply(claims);
   }
   public String extractSubject(String token) {
    return extractClaims(token, Claims::getSubject);
   }
   public boolean isTokenexpired(String token) {
    return extractClaims(token, Claims::getExpiration).before(new Date());
   }

   
    
}
