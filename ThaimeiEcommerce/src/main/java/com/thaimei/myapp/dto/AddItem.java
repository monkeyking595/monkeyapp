package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItem {
    @NotNull(message="Product ID cannot be null")
    private Long productId;
    @Positive(message="Quantity cannot be negative")
    private int quantity;
}
