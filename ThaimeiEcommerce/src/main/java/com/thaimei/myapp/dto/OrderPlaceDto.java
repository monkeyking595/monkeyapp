package com.thaimei.myapp.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderPlaceDto {
    @NotNull(message = "Product ID cannot be null")
    private long productId;

    @NotNull(message = "Quantity cannot be null")
    private int quantity;
    
}
