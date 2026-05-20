package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.ProductDto;
import  com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import com.thaimei.myapp.repository.ProductsRepo;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.repository.StoreRepo;
import com.thaimei.myapp.dto.AddProductDto;



@Service
public class ProductService {
    private final ProductsRepo productsRepo;
    private final StoreRepo storeRepo;
    public ProductService(ProductsRepo productsRepo, StoreRepo storeRepo) {
        this.productsRepo = productsRepo;
        this.storeRepo = storeRepo;
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
            product.getCategory(),
            product.getColor(),
            product.getSize()
        ))
        .toList();
    }
    public ProductDto getProductById(long id) {
        ProductsModel product = productsRepo.findById(id)
        .orElseThrow(()-> new RuntimeException("Product cannot be found"));

        return new ProductDto(
            product.getProductId(),
            product.getName(),
            product.getPrice(),
            product.getDescription(),
            product.getImageURL(),
            product.getQuantity()
            product.getCategory(),
            product.getColor(),
            product.getSize()
        );

    }
    public void saveProducts(AddProductDto productDto, User user) {
        StoreModel store = storeRepo.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("Store not found for the given User"));
        if(productsRepo.existByStoreCategoryColorSize(store, productDto.getCategory(), productDto.getColor(), productDto.getsize())) {
            throw new IllegalArgumentException("Product with the same category, color, and size already exists in the store");
        }
    }

   
    
}
