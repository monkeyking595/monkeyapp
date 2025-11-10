package com.thaimei.myapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/Product")
public class ProductDetails {
    private final  ProductService productService;
    public ProductDetails(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/productDetails/{id}")
    public ResponseEntity<ProductDto> getProductDetails(@PathVariable Long id) {
        ProductDto productDetail= productService.getProductById(id);
        return ResponseEntity.ok(productDetail);
    }
    
    
}
