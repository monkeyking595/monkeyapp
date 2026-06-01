package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.thaimei.myapp.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.AddItem;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.thaimei.myapp.security.CustomUserDetails;
import java.util.Map;



@RestController
@RequestMapping("/Cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/getItems")
    public ResponseEntity<?> getMyCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return  ResponseEntity.ok(cartService.getCartByUserId(userDetails.getId()));
    }
    
    @PostMapping("/AddItems")
    public ResponseEntity<?> addItems(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AddItem addItem) {
        long userId = userDetails.getId();
        cartService.addItemsToCart(addItem, userId);
        return ResponseEntity.ok(Map.of("message","Items added to cart"));

    } 
}
    


    

