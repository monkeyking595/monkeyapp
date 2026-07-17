package com.thaimei.myapp.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderPlaceDto {
    @NotEmpty(message ="OrderItems cannot be empty")
    @Valid 
    private List<ItemRequestDto> orderItems;
} 

