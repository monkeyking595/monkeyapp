package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.OrderPlaceDto;
import com.thaimei.myapp.dto.OrderResponseDto;
import com.thaimei.myapp.error.AppException;
import com.thaimei.myapp.service.OrderService;

import jakarta.validation.Valid;

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

    @PostMapping("/buyNowCheckout")
    public ResponseEntity<?> buynow (@Valid @RequestBody OrderPlaceDto orderDto, @AuthenticationPrincipal CustomUserDetails principal) {
        if (orderDto.getOrderItems().size() != 1) {
            throw new AppException("buynow excepts only one item", 400);
        }
        User  user=principal.getUser();
        orderService.checkout(orderDto, user);
        return ResponseEntity.ok(Map.of("message","Order placed successfully!"));
    }

    @PostMapping("/CartCheckout")
    public ResponseEntity<?> checkoutFromCart (@Valid @RequestBody OrderPlaceDto orderDto, @AuthenticationPrincipal CustomUserDetails principal) {
        User  user=principal.getUser();
        orderService.checkout(orderDto, user);
        return ResponseEntity.ok(Map.of("message","Order placed successfully!"));
    }

    
    @GetMapping("/GetOrder")
    public ResponseEntity<List<OrderResponseDto>> getOrders(@AuthenticationPrincipal CustomUserDetails principal) {
        User user=principal.getUser();
        List<OrderResponseDto>orders=orderService.getOrdersByUserId(user);
        return ResponseEntity.ok(orders);
    }
}
