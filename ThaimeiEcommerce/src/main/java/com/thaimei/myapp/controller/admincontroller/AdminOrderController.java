package com.thaimei.myapp.controller.admincontroller;
import com.thaimei.myapp.service.OrderService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import com.thaimei.myapp.dto.OrderDto;
import java.util.List;

@RestController
public class AdminOrderController {
    private final OrderService orderService;
    public AdminOrderController(OrderService orderService) {
        this.orderService=orderService;
    
    }
    
}
