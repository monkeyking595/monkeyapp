package com.thaimei.myapp.controller;

import org.springframework.http.ResponseEntity;
import  com.thaimei.myapp.dto.ProductDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping
public class ProductListController {
    private final ProductService productService;
    public ProductListController(ProductService productService) {
        this.productService=productService;
    }

    @GetMapping("/productsList")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getProducts();
        return ResponseEntity.ok(products);
        
    }
    
}
