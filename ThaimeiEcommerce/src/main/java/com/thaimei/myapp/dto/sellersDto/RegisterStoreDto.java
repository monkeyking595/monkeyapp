package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import com.thaimei.myapp.enums.BusinessType;
import jakarta.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStoreDto {
    @NotBlank(message="brand/store name cannot be blank")
    private String storeName;
    @NotBlank(message="business type cannot be blank")
    private BusinessType businessType;   
    @NotNull(message="latitude cannot be null")
    private Double latitude;
    @NotNull(message="longitude cannot be null")
    private Double longitude;
}
