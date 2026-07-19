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
    // @Valid, it handles cascading validation into nested objects, without this the validation annotation in the ItemRquestDto will be ignore by the validator. it's telling the validator to one level deeper.
    @Valid 
    private List<ItemRequestDto> orderItems;
} 

