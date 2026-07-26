package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

import com.thaimei.myapp.enums.OpenCloseStore;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserStoreDto {
    
    private Long storeId;
    
    private String storeName;

    private List<ProductDto> products;

    private OpenCloseStore openCloseStore;
}
