package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminUserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String status;

}