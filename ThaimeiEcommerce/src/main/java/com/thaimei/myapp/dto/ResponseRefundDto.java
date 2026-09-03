package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;


import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRefundDto {
    private String status;
    private String ItemName;
    private Integer quantity;
}
