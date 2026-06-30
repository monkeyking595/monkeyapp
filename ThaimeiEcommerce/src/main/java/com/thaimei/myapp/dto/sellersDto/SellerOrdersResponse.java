package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SellerOrdersResponse {
    private Long orderId;
    private String name;
    private int totalPrice;
    
}
