package com.thaimei.myapp.dto;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    @NotNull(message="Id cannot be Null")
    private Long cartId;

    
    private List<CartItemDto> items;

    @PositiveOrZero(message="Total price cannot be negative")
    BigDecimal totalPrice;
    
    @PositiveOrZero(message="Quantity cannot be negative")
    private int totalquantity;
}
