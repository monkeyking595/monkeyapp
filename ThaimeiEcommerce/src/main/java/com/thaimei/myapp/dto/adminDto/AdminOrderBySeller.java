package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import com.thaimei.myapp.model.OrderItems;
import com.thaimei.myapp.enums.OrderStatusEnum;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdminOrderBySeller {
    private Long id;
    private Long sellerId;
    private String sellerName;
    private Long storeId;
    private String storeName;
    private List<OrderItems> orderItems;
    private OrderStatusEnum status;
    private LocalDateTime orderedDate;
    
}
