package com.thaimei.myapp.security;
import io.jsonwebtoken.*;
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
        //hmacShaKeyFor() method is used to create a SecretKey object from the provided secret key string. The secret key is decoded from Base64 format before being used to create the SecretKey object.
        this.key= Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)); 
        //wraps the key with secretKeyspec and leabel it with HmacSHA256 algorithm, this will return a secretKey object 
    }
   public String generateToken(String subject, long expirationMillis) {
    Map<String, Object> claims= new HashMap<>();
    return Jwts.builder()
    //the new version of jjwt doesn't need the set() method to set the claims 
        .claims(claims)
        .subject(subject)
        //uses inline object creation
        .expiration(new Date(System.currentTimeMillis()+expirationMillis))
        //here the System is whatever machine JVM is running on
        .issuedAt(new Date(System.currentTimeMillis()))
        .issuer("ThaimeiEcommerce")
        //the add() method is used to set the audience value and and() is used to continue to the chain
        .audience().add("ThaimeiEcommerceUsers").and()
        //Hmac computation happens here, the key is used to double hash the token
        .signWith(key)
        //this actually produces the finished token string, before this was just setup, everything happens here computation, concatination of strings, Base64 encoding etc.
        .compact();
    }
   public Claims extractAllClaims(String token) throws JwtException {
        //returns an empty parser builder
    return Jwts.parser()
        //secretKey is registered here 
         .verifyWith(key)
         //takes all the configured settings and return the actual parser which can be use to parse the token, before this it's just settings for the parser
         .build()
         //re-computation of the signature happens here
         //returns jws<Claims> the whole pared token
         .parseSignedClaims(token)
         //if the signature is valid, the payload is extracted and stored in claims 
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
