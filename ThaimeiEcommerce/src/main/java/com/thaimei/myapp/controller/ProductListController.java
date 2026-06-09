package com.thaimei.myapp.controller;

import org.springframework.http.ResponseEntity;
import  com.thaimei.myapp.dto.ProductDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thaimei.myapp.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/products")
public class ProductListController {
    private final ProductService productService;
    public ProductListController(ProductService productService) {
        this.productService=productService;
    }

    @GetMapping("/productsList")
    public ResponseEntity<Slice<ProductDto>> getAllProducts(@RequestParam (defaultValue = "0")int page, @RequestParam ( defaultValue = "20")int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("price").ascending().and (Sort.by("productId").ascending()));
        Slice<ProductDto> products = productService.getProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
}
