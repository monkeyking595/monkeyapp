package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.OrderPlaceDto;
import com.thaimei.myapp.dto.OrderResponseDto;
import com.thaimei.myapp.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.repository.UserRepository;
import com.thaimei.myapp.model.User;
import java.util.Map;


@RestController
@RequestMapping("/Orders")
public class OrderController {
    private final OrderService orderService;
    
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
    }

    @PostMapping("/PlaceOrder")
    public ResponseEntity<?> placedOrder(@RequestBody OrderPlaceDto orderDTo, @AuthenticationPrincipal CustomUserDetails principal) {
        User  user=principal.getUser();
        orderService.saveOrders(orderDTo, user);
        return ResponseEntity.ok(Map.of("message","Order placed successfully!"));
    }
    @GetMapping("/GetOrder")
    public ResponseEntity<List<OrderResponseDto>> getOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        User user=principal.getUser();
        List<OrderResponseDto>orders=orderService.getOrdersByUserId(user);
        return ResponseEntity.ok(orders);
    }
    
    
    
    

}
