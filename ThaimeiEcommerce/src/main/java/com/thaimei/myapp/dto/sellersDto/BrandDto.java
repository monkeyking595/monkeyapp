package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    @NotBlank(message="brand/store name cannot be blank")
    private String brandName;
    @NotBlank(message="business type cannot be blank")
    private String businessType;   
}
