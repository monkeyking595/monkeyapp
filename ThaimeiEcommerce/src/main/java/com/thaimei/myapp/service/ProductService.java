package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.ProductDto;
import  com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import com.thaimei.myapp.repository.ProductsRepo;
import org.springframework.stereotype.Service;



@Service
public class ProductService {
    private final ProductsRepo productsRepo;
    public ProductService(ProductsRepo productsRepo) {
        this.productsRepo = productsRepo;
    }

    public List<ProductDto> getProducts() {
        List<ProductsModel> Allproducts = productsRepo.findAll();
        return Allproducts.stream()
        .map(product -> new ProductDto(
            product.getProductId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getImageURL(),
            product.getQuantity()
        ))
        .toList();
    }
    public ProductDto getProductById(Long id) {
        ProductsModel product = productsRepo.findById(id)
        .orElseThrow(()-> new RuntimeException("Product cannot be found"));

        return new ProductDto(
            product.getProductId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getImageURL(),
            product.getQuantity()
        );

    }

   
    
}
