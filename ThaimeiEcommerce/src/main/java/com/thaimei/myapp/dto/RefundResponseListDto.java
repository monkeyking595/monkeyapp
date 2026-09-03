package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import com.thaimei.myapp.dto.ResponseRefundDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponseListDto {
    private List<ResponseRefundDto> refundResponseList;
    private LocalDateTime completedAt;    
}
