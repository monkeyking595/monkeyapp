package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SellersLoginDto {
    @NotBlank(message="sellersName cannot be blank")
    private String sellersName;
    @NotBlank(message="sellersPassword cannot be blank")
    private String sellersPassword;
    
}
