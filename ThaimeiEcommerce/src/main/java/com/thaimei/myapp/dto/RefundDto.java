package com.thaimei.myapp.dto;
import lombok.Data;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundDto {
    @NotNull
    @NotEmpty(message="this field cannot be empty")
    
    @Valid
    private List<RefundItemRequestDto> items;
}
