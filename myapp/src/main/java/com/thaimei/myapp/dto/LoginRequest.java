package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message =" Username cannot be blank")
    private String username;
    @NotBlank(message ="password cannot be blnak")
    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password =  password;
    }
    
}
