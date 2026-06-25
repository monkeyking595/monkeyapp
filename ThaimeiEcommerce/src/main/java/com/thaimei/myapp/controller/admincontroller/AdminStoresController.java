package com.thaimei.myapp.controller.admincontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thaimei.myapp.dto.adminDto.AdminStoreApprovalDto;
import com.thaimei.myapp.security.CustomUserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import com.thaimei.myapp.service.StoreService;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import com.thaimei.myapp.dto.adminDto.AdminStoresDto;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/admin/api")
public class AdminStoresController {

    private final StoreService storeService;
    public AdminStoresController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PatchMapping("/approveStores/{storeId}")
    public ResponseEntity<Map<String, String>> storesApproval (@PathVariable Long storeId, @AuthenticationPrincipal CustomUserDetails customUserDetails,@Valid @RequestBody AdminStoreApprovalDto dto ) {
        Long userId = customUserDetails.getId();
        storeService.updateStoreStatus(storeId,userId,dto);
        return ResponseEntity.ok(Map.of("message","Store updated successfully!"));
    }
    
    @GetMapping("/getAllStoresBySeller/{sellerId}")
    public ResponseEntity<List<AdminStoresDto>> getStoresByUser(@PathVariable Long sellerId) {
        List<AdminStoresDto> stores = storeService.getAllStoresBySeller(sellerId);
        return ResponseEntity.ok(stores);
    }

    //feature that could be added in the future
    // 1. check all pending user status.
}
