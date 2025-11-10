package com.thaimei.myapp.dto.adminDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class AdminRegistrationDto {
    @NotBlank(message = "Username cannot be blank")
    @Size(min=3, max=20, message ="Username must be between 3 and 20 chracters")
    private String adminname;
    @NotBlank(message = "Password cannot be blank")
    @Size(min=8, max=20, message = "Password must be between 8 and 20 chracters")
    private String adminpassword;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Use the correct email format")
    @Size(max=50, message = "Email must be less than 50 chracters")
    private String adminemail;
    @NotBlank(message= "This field ir required")
    @Size(min=8, max=20, message = "Confirm password must be between 8 and 20 charcters")
    private String adminconfirmpassword;

    public String getAdminname() {
        return adminname;
    }
    public void setAdminname(String adminname) {
        this.adminname=adminname;
    }
    public String getAdminpassword() {
        return adminpassword;
    }
    public void setAdminpassword(String adminpassword) {
        this.adminpassword=adminpassword;
    }
    public String getAdminconfirmpassword() {
        return adminconfirmpassword;
    }
    public void setAdminconfirmPassowrd(String adminconfirmpassword) {
        this.adminconfirmpassword=adminconfirmpassword;
    }
    public String getAdminemail() {
        return adminemail;
    }
    public void setAdminemail(String adminemail) {
        this.adminemail=adminemail;
    }
    
}