package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.enums.OpenCloseStore;


import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StoresDto {
   
    private Long storeId;
    
    private String storeName;

    private List<ProductDto> products;

    private OpenCloseStore openCloseStore;
}
