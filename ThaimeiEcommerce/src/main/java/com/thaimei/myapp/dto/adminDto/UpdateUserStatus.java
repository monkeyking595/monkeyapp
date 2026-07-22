package com.thaimei.myapp.dto.adminDto;
import com.thaimei.myapp.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateUserStatus {
    
    // frontend sends the status and this accepts it.
    @NotNull(message  ="status cannot be empty")
    private UserStatus userStatus;
}
