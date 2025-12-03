package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull(message="ID cannot be null")
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String productName;
    @Positive(message="Quantity cannot be negavtive")
    private int quantity;
    @NotBlank(message = "Status cannot be blank")
    private String status;
    @Positive(message = "Total price cannot be negative")
    private double totalPrice;
    @NotBlank(message = "image cannot be blank")
    private String imageURL;

}
