package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.RoleEnum;
import com.thaimei.myapp.enums.UserStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminUserDto {
    private Long id;
    private String username;
    private String email;
    private RoleEnum role;
    private UserStatus status;
}