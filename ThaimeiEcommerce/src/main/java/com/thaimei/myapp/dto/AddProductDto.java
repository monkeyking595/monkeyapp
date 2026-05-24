package com.thaimei.myapp.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.Category;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.Size;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class AddProductDto {
    @NotNull(message = "Store Id cannot be null")
    private Long storeId;
    @NotBlank(message ="Product name cannot be blank")
    private String name;
    @NotNull(message="price cannot be null")
    @Positive(message = "Price cannot be negative")
    private BigDecimal price;
    @NotBlank(message="description cannot be blank")
    private String description;
    @NotBlank(message="Image cannot be blank")
    private String imageURL;
    @Positive(message="Quantity cannot be negative")
    private int quantity;
    @NotNull(message="Category cannot be null")
    private Category category;
    @NotNull(message="Color cannot be null")
    private Color color;
    @NotNull(message="Size cannot be null")
    private Size size;
    
}
