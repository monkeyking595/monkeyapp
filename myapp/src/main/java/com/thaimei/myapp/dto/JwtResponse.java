package com.thaimei.myapp.dto;

public class JwtResponse {
    String token;
    String username;

    public JwtResponse(String token, String username) {
        this. token = token;
        this.username = username;
    }
    public String getToken() {
        return token;
    }
    public String getUsername() {
        return username;
    }
    public void setToken(String token) {
        this.token=token;
    }
    public void setUsername(String username) {
        this.username=username;
    }


    
}
