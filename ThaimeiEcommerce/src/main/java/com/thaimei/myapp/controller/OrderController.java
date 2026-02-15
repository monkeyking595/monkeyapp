package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.OrderDto;
import com.thaimei.myapp.service.OrderService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
@RequestMapping("/Orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/PlaceOrder")
    public ResponseEntity<String> placedOrder(@RequestBody OrderDto orderDTo) {
        orderService.saveOrders(orderDTo);
        return ResponseEntity.ok("Order placed successfully!");
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> getOrders(@PathVariable Long userId) {
        List<OrderDto>orders=orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);

    }
    
    
    
    

}
