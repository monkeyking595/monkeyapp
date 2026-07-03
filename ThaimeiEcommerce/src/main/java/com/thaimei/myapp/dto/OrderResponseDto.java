package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

import com.thaimei.myapp.enums.OrderStatusEnum;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderResponseDto {
    
    private String productName;
    
    private int quantity;
    
    private OrderStatusEnum status;
    
    private BigDecimal totalPrice;
    
    private String imageURL;

}
