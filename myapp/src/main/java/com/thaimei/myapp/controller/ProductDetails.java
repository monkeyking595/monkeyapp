package com.thaimei.myapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/Product")
public class ProductDetails {
    private final  ProductService productService;
    public ProductDetails(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/productDetails")
    public ResponseEntity<ProductDto> getProductDetails() {
        ProductDto productDetail= productService.getProductById();
        return ResponseEntity.ok(productDetail);
    }
    
    
}
