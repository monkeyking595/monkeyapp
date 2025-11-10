package com.thaimei.myapp.dto.adminDto;
import jakarta.validation.constraints.NotBlank;

public class AdminLoginDto {
    @NotBlank(message="username cannot be blank")
    private String  adminUsername;
    @NotBlank(message="password cannot be blank")
    private String adminPassword;

    public String getAdminUsername() {
        return adminUsername;
    }
    public void setAdminUsername(String adminUsername) {
        this.adminUsername=adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword=adminPassword;
    }


}