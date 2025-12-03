package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {
    @NotBlank(message ="Username cannot be blank")
    private String username;
    @NotBlank(message ="password cannot be blank")
    private String password;
    
}
