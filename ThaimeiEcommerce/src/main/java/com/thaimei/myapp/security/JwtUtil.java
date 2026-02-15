package com.thaimei.myapp.security;
import io.jsonwebtoken.*; 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final SecretKey key;
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("JWT SECRET INJECTED => " + secret);
        this.key= Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));  
    }
   public String generateToken(String subject, long expirationMillis) {
    Map<String, Object> claims= new HashMap<>();
    return Jwts.builder()
        .claims(claims)
        .setSubject(subject)
        .setExpiration(new Date(System.currentTimeMillis()+expirationMillis))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setIssuer("ThaimeiEcommerce")
        .setAudience("ThaimeiEcommerceUsers")
        .signWith(key)
        .compact();

   }
   public Claims extractAllClaims(String token) throws JwtException {
    return Jwts.parser()
         .verifyWith(key)
         .build()
         .parseSignedClaims(token)
         .getPayload();                 
   }
   
    public<T> T extractClaims(String token, Function<Claims, T> ClaimResolver) {
        final Claims claims=extractAllClaims(token);
        return ClaimResolver.apply(claims);
   }
   public String extractSubject(String token) {
    return extractClaims(token, Claims::getSubject);
   }
   public boolean isTokenExpired(String token) {
    return extractClaims(token, Claims::getExpiration).before(new Date());
   }

   
    
}
