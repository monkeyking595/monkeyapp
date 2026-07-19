package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    
    private Long itemId;

  
    private int quantity;


    private BigDecimal price;

   
    private String productName;
    
  
    private BigDecimal totalPrice;
    
    private String imageURL;

    private String description;
}
