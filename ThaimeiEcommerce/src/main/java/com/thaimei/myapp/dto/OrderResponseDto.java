package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    
    private String productName;
    
    private int quantity;
    
    private String status;
    
    private double totalPrice;
    
    private String imageURL;

}
