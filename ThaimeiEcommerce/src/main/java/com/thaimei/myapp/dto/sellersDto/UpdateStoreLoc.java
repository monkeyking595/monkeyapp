package com.thaimei.myapp.dto.sellersDto;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; 
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateStoreLoc {
    @NotNull
    @DecimalMin(value ="-90", message ="latitude must be >= -90")
    @DecimalMax(value ="90", message ="latitude must be <= 90")
    private Double latitude;

    @NotNull
    @DecimalMin(value ="-180", message ="longitude must be >= -180")
    @DecimalMax(value ="180", message ="longitude must be <= 180")
    private Double longitude;
    
}
