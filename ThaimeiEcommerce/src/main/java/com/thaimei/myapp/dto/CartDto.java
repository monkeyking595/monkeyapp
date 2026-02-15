package com.thaimei.myapp.dto;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    @NotNull(message="Id cannot be Null")
    private Long cartId;

    @NotEmpty(message="items list cannot be empty")
    private List<CartItemDto> items;

    @Positive(message="Total price cannot be negative")
    double totalPrice;
    
    @Positive(message="Quantity cannot be negative")
    private int quantity;
}
