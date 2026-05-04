package com.thaimei.myapp.controller.admincontroller;
import com.thaimei.myapp.service.OrderService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.thaimei.myapp.dto.adminDto.AdminOrderDto;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/admin/api")
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService) {
        this.orderService=orderService;
    }

    @GetMapping("/adminOrders")
    public ResponseEntity<List<AdminOrderDto>> getAllOrders() {
        var adminOrders = orderService.getAdminOrders();
        return ResponseEntity.ok(adminOrders);

    }
    
}
