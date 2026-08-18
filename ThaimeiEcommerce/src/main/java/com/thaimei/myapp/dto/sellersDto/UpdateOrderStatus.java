package com.thaimei.myapp.dto.sellersDto;

import com.thaimei.myapp.enums.OrderStatusEnum;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateOrderStatus {
    @NotNull(message ="This field cannot be null")
    private OrderStatusEnum status;
}
