package com.thaimei.myapp.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.dto.CartDto;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.thaimei.myapp.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.thaimei.myapp.dto.AddItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.thaimei.myapp.security.CustomUserDetails;




@RestController
@RequestMapping("/Cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping("/getItems")
    public ResponseEntity<?> getMyCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return cartService.getCartByUserId(userDetails.getId())
        .map(myCart -> {
             if (myCart.getItems().isEmpty()) {
                return ResponseEntity.status(404).body("Cart is empty");
            }
        return ResponseEntity.ok(myCart);
        })
        .orElseGet(() -> ResponseEntity.status(404).body("Cart is empty"));    
    }
    
    @PostMapping("/AddItems")
    public ResponseEntity<String> addItems(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AddItem addItem) {
        Long userId = userDetails.getId();
        cartService.addItemsToCart(addItem, userId);
        return ResponseEntity.ok("Items added to cart");

    } 
}
    


    

