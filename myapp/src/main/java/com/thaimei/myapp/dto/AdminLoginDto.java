package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;

public class AdminLoginDto {
    @NotBlank(message="username cannot be blank")
    private String  adminusername;
    @NotBlank(message="password cannot be blank")
    private String adminpassword;

    public String getAdminusername() {
        return adminusername;
    }
    public void setAdminusername(String adminusername) {
        this.adminusername=adminusername;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword=adminpassword;
    }


}