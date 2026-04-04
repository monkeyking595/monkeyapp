package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.OrderDto;
import com.thaimei.myapp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.repository.UserRepository;


@RestController
@RequestMapping("/Orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @PostMapping("/PlaceOrder")
    public ResponseEntity<String> placedOrder(@RequestBody OrderDto orderDTo, @AuthenticationPrincipal CustomUserDetails principal) {
        long userId=principal.getId();
        userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("user not found"));
        orderService.saveOrders(orderDTo);
        return ResponseEntity.ok("Order placed successfully!");
    }
    @GetMapping("/GetOrder")
    public ResponseEntity<List<OrderDto>> getOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        long userId=principal.getId();
        userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("user not found"));
        List<OrderDto>orders=orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
    
    
    
    

}
