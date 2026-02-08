package com.thaimei.myapp.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    @NotNull(message ="Id cannot be null")
    private Long productId;
    @NotBlank(message ="Product name cannot be blank")
    private String name;
    @Positive(message="price cannot be negative")
    private BigDecimal price;
    @NotBlank(message="description cannot be blank")
    private String description;
    @NotBlank(message="Image cannot be blank")
    private String imageURL;
    @Positive(message="Quantity cannot be negative")
    private int quantity;
    
    
}
