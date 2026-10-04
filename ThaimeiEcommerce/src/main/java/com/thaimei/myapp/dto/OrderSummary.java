package com.thaimei.myapp.dto;
import com.thaimei.myapp.enums.OrderStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    private Long orderId;
    private OrderStatusEnum status;
}
