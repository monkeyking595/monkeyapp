package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.thaimei.myapp.enums.PaymentStatus;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {


    private List<Long> orderIds;

    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private String currency;
}
