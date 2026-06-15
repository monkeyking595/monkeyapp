package com.thaimei.myapp.dto.adminDto;
import com.thaimei.myapp.enums.StoreStatus;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStoreApprovalDto {
@NotNull (message = "Satus cannot be null")
private StoreStatus status;
}