package com.thaimei.myapp.controller.sellers;
import com.thaimei.myapp.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.thaimei.myapp.service.ProductService;

@RestController
@RequestMapping("/seller")
public class AddProducts {
    private final ProductService productService;
    public AddProducts(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/addProducts")
    public ResponseEntity<String> addProducts(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok("product added successfully");

    }
    
}
