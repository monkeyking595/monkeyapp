package com.thaimei.myapp.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserSideResponseRefundDto {
    private List<UserSideRefundDto> refunds;
    
}
