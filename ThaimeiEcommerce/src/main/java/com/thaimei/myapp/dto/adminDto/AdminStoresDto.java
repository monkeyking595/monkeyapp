package com.thaimei.myapp.dto.adminDto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.thaimei.myapp.enums.StoreStatus;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminStoresDto {
    private Long sellerId;
    private Long storeId;
    private StoreStatus status;
    private String name;
}
