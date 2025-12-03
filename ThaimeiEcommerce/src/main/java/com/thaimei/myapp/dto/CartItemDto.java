package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    @NotNull(message="Id cannot be null")
    private Long productId;
    @Positive(message="Quantity cannot be negative")
    private int quantity;
    @Positive(message="Price cannot be negative")
    private double price;
    @NotBlank(message="Product name cannot be blank")
    private String productName;
    
}
