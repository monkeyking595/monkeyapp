package com.thaimei.myapp.dto.sellersDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.thaimei.myapp.enums.OpenCloseStore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenStoreDto {
    @NotNull(message = "this field cannot be null")
    private OpenCloseStore openCloseStore = OpenCloseStore.OPEN;
}
