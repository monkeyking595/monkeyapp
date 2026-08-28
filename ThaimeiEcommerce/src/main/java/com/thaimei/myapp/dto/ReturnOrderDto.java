package com.thaimei.myapp.dto;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReturnOrderDto {
    @NotNull(message = "this field cannot be null")
    @NotEmpty(message = "ItemIds cannot be empty")
    private List<Long> itemIds;
}
