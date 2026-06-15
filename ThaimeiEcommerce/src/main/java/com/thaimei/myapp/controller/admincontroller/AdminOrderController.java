package com.thaimei.myapp.controller.admincontroller;
import com.thaimei.myapp.service.OrderService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.dto.adminDto.AdminOrderDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.thaimei.myapp.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequestMapping("/admin/api")
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService) {
        this.orderService=orderService;
    }
    //audit log is yet to be integrated..
    @GetMapping("/adminOrders")
    public ResponseEntity<Slice<AdminOrderDto>> getAllOrders(@RequestParam (defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getId();
        Pageable pageable = PageRequest.of(page, size);
        var adminOrders = orderService.getAdminOrders(userId, pageable);
        return ResponseEntity.ok(adminOrders);
    } 
}
