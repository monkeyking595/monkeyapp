package com.thaimei.myapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.StoreService;
import com.thaimei.myapp.dto.UserStoreDto;


@RestController
@RequestMapping("/customers")

public class CustomerSideStoreController {
    private final StoreService storeService;

    public CustomerSideStoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/search/store")
    public ResponseEntity<UserStoreDto> searchStore(@RequestParam String storeName) {
        return ResponseEntity.ok(storeService.findStoreByName(storeName));
    }    
}
