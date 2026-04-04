package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import com.thaimei.myapp.dto.OrderDto;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOrderDto {
    @NotEmpty(message="orders cannot be empty")
    private List<OrderDto> orders;
}
