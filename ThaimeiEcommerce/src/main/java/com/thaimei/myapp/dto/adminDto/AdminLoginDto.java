package com.thaimei.myapp.dto.adminDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminLoginDto {
    @NotBlank(message="username cannot be blank")
    private String  adminUsername;
    @NotBlank(message="password cannot be blank")
    private String adminPassword;

}