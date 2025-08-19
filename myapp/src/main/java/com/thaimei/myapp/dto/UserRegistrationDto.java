package com.thaimei.myapp.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {
    @NotBlank(message = "Username is required")
    @Size(min=3, max=20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message ="Password is required")
    @Size(min=6, max=100, message= "Password must be atleast beween 6 and 100 charcters")
    private String password;

    @NotBlank(message = "confirm password is required")
    @Size(min=6, max=100, message = "Confirm password must be atleast between 6 and 100 charcters")
    private String confirmpassword;

    @NotBlank(message =" Email is required")
    @Email(message = "email should be valid")
    @Size(max=50, message = "Email must be less than 50 characters")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) { 
        this.password = password;
    }
    public String getConfirmpassword() {
        return confirmpassword;
    }
    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    
}
