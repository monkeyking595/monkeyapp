package com.thaimei.myapp.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.thaimei.myapp.enums.IsOpen;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserStoreDto {
    @NotBlank(message = "store name cannot be blank")
    private String storeName;

    @NotNull(message = "this cannot be null")
    private IsOpen isOpen;
}
