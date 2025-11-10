package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    @NotBlank(message="PaymentId cannot be blank")
    private String paymentId;
    @NotBlank(message="OrderId cannot be blank")
    private String orderId;
    @NotBlank(message="Payment Status cannot be blank")
    private String status;
    @Positive(message="Total amount cannot be negative")
    private double totalAmount; 
    @NotBlank(message="Payment Method cannot be blank")
    private String paymentMethod;
    @NotBlank(message="Signature cannot be empty")
    private String signature;
    @NotBlank(message = "Currency cannot be empty")
    private String currency;

}
