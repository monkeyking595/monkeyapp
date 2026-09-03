package com.thaimei.myapp.controller.admincontroller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.RefundService;
import com.thaimei.myapp.dto.RefundResponseListDto;

@RestController
@RequestMapping("/admin")
public class ApproveRefund {
    private final RefundService refundService;

    public ApproveRefund(RefundService refundService) {
        this.refundService = refundService;
    }

    @PatchMapping("/refund/approve/{id}")
    public ResponseEntity<?> approveRefund (@PathVariable Long id) {
        refundService.approveRefund(id);
        return ResponseEntity.ok(Map.of("message", "Refund approved"));
    }

    @GetMapping("/refund/pullrefund")
    public ResponseEntity<RefundResponseListDto> pullRefund () {
        RefundResponseListDto returnRefunds = refundService.getRefunds();
        return ResponseEntity.ok(returnRefunds);
    }
}
