package com.thaimei.myapp.controller.admincontroller;
import org.springframework.web.bind.annotation.RestController;

import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.enums.ProductStatus;
import com.thaimei.myapp.error.ResourceNotFoundException;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.repository.UserRepository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import com.thaimei.myapp.service.ProductService;

@RestController
@RequestMapping("/admin/api")
public class AdminProductsController {
    private final UserRepository userRepository;
    private final ProductService productService;
    public AdminProductsController (UserRepository userRepository, ProductService productService) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<Slice<ProductDto>> getAllProductsForAdmin (@PathVariable Long userId, @RequestParam(defaultValue ="0") int page, @RequestParam (defaultValue ="20") int size) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("seller not found"));
        Pageable pageable = PageRequest.of(page, size);
        Slice <ProductDto> products = productService.getProductsForSeller(user, pageable);
        return ResponseEntity.ok(products);
    }

    //bulk request the frontend sends a list of ids.
    @PatchMapping("/store/{storeId}/products")
    public ResponseEntity<?> updateProductStatus (@PathVariable Long storeId,  @RequestBody List<Long> productIds, @RequestParam ProductStatus status) {
        productService.updateProductsStatus(storeId, productIds, status);
        return ResponseEntity.ok(Map.of("message","product status updated successfully"));
    }
    
}
