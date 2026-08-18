package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import com.thaimei.myapp.enums.BusinessType;
import jakarta.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterStoreDto {
    @NotBlank(message="brand/store name cannot be blank")
    private String storeName;

    @NotNull(message="business type cannot be blank")
    private BusinessType businessType;   

    @NotNull(message="latitude cannot be null")
    @DecimalMin(value = "-90.0", message="latitude must be >=-90")
    @DecimalMax(value = "90.0", message ="latitude must be <= 90")
    private Double latitude;
    
    @NotNull(message="longitude cannot be null")
    @DecimalMin(value = "-180.0", message ="longitude must be >=-180")
    @DecimalMax(value = "180.0", message ="longitude must be <=180")
    private Double longitude;
}
