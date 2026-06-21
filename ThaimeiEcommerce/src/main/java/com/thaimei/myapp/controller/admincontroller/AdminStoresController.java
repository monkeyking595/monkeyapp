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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import com.thaimei.myapp.dto.adminDto.AdminStoresDto;
import java.util.List;



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
    
    @GetMapping("/getAllStoresForAdmin/{sellerId}")
    public ResponseEntity<List<AdminStoresDto>> getStoresForAdmin ( @PathVariable Long sellerId) {
        storeService.getAllStoresBySeller(sellerId);


    }
}
