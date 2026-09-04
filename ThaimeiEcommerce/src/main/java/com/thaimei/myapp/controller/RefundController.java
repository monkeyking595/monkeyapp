package com.thaimei.myapp.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thaimei.myapp.dto.RefundDto;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.service.RefundService;
import com.thaimei.myapp.dto.UserSideResponseRefundDto;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/customer")
public class RefundController {
    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService=refundService;
    }

    @PostMapping("/requestRefund")
    public ResponseEntity<?> recordRefund(@Valid @RequestBody RefundDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        refundService.requestRefund(dto, userId);
        return ResponseEntity.ok(Map.of("message","Refunded"));
    }

    @GetMapping("/getRefunds")
    public ResponseEntity<UserSideResponseRefundDto> getRefunds(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserSideResponseRefundDto dtos =refundService.getRefundForUser(userDetails.getId());
        return ResponseEntity.ok(dtos);
    }
}
