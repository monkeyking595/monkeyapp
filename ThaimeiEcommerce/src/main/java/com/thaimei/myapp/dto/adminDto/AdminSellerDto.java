package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.RoleEnum;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminSellerDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private RoleEnum status;
}
