package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.OrderStatusEnum;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderResponseDto {
    
    private String productName;
    
    private int quantity;
    
    private OrderStatusEnum status;
    
    private double totalPrice;
    
    private String imageURL;

}
