package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StoresDto {
    @NotNull(message="Id cannot be null")
    private Long storeId;
    @NotBlank(message= "Store name cannot be empty")
    private String storeName;
}
