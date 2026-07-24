package com.thaimei.myapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.param.issuing.AuthorizationCreateParams.MerchantData.Category;
import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.Size;
import com.thaimei.myapp.service.ProductService;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/product")
public class ProductDetails {
    private final  ProductService productService;
    public ProductDetails(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping("/details/{id}")
    //pulls a specific product using the id sent from the product listing.
    public ResponseEntity<ProductDto> getProductDetails(@PathVariable Long id) {
        ProductDto productDetail= productService.getProductById(id);
        return ResponseEntity.ok(productDetail);
    }

    @GetMapping("/search") 
    // required = false, an attribute of @RequestParameter which allow the request to be null/empty, by default it's required = true implicit.
    public ResponseEntity<Slice<ProductDto>> searchProducts(@RequestParam(required = false) String q,
        @RequestParam(required = false) Color color,
        @RequestParam(required = false) Size size,
        @RequestParam(required = false) Category category,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam (defaultValue ="0") int page, @RequestParam (defaultValue ="20") int pageSize) {

        Pageable  pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(productService.searchProduct(q,color,size,category,minPrice,maxPrice, pageable));
    }
}
