package com.thaimei.myapp.controller.sellers;
import com.thaimei.myapp.dto.AddProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.thaimei.myapp.service.ProductService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.thaimei.myapp.security.CustomUserDetails;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
@RestController
@RequestMapping("/sellers")
public class SellerProductsController {
    private final ProductService productService;
    public SellerProductsController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping("/addProducts")
    public ResponseEntity<?> addProducts(@Valid @RequestBody AddProductDto addProductDto, @AuthenticationPrincipal CustomUserDetails customUserDetails ) {
        User user = customUserDetails.getUser();
        productService.saveProducts(addProductDto, user);
        //since success message is just a plain string we should wrap it in Map.of() so frontend never reveives plain string but always a JSON object.
        return ResponseEntity.ok(Map.of("message","product added successfully"));
    }

    @GetMapping("/getProducts")
    public ResponseEntity<Slice<ProductDto>> getProductsForSeller(@AuthenticationPrincipal CustomUserDetails customUserDetails , @RequestParam (defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        User user = customUserDetails.getUser();
        Pageable pageable = PageRequest.of(page, size);
        Slice <ProductDto> products = productService.getProductsForSeller(user, pageable);
        return ResponseEntity.ok(products);
    }
    
}
