package com.thaimei.myapp.controller.sellers;
import com.thaimei.myapp.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.thaimei.myapp.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.model.User;

@RestController
@RequestMapping("/seller")
public class AddProducts {
    private final ProductService productService;
    public AddProducts(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/addProducts")
    public ResponseEntity<String> addProducts(@Valid @RequestBody ProductDto productDto, @AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        User user = customUserDetails.getUser();
        productService.saveProducts(productDto, user);
        return ResponseEntity.ok("product added successfully");

    }
    
}
