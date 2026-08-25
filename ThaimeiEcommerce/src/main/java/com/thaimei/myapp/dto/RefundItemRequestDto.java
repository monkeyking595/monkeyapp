package com.thaimei.myapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RefundItemRequestDto {

   @NotNull(message ="Id cannot be Empty")
   private Long itemId;

    @Min(value = 1, message ="Quantity cannot be less than 1")
    @NotNull(message ="quantity cannot be null")
    private Integer quantity;
}
