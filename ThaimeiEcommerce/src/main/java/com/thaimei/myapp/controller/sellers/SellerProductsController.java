package com.thaimei.myapp.controller.sellers;
import com.thaimei.myapp.dto.AddProductDto;
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
import java.util.List;
import com.thaimei.myapp.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequestMapping("/seller")
public class SellerProductsController {
    private final ProductService productService;
    public SellerProductsController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/addProducts")
    public ResponseEntity<String> addProducts(@Valid @RequestBody AddProductDto addProductDto, @AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        User user = customUserDetails.getUser();
        productService.saveProducts(addProductDto, user);
        return ResponseEntity.ok("product added successfully");
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDto>> getProductsForSeller(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<ProductDto> products = productService.getProductsForSeller(user);
        return ResponseEntity.ok(products);
    }
    
}
