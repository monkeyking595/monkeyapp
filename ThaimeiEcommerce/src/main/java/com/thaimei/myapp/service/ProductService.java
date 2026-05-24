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
import org.modelmapper.ModelMapper;



@Service
public class ProductService {
    private final ProductsRepo productsRepo;
    private final StoreRepo storeRepo;
    private final ModelMapper modelMapper;
    public ProductService(ProductsRepo productsRepo, StoreRepo storeRepo, ModelMapper modelMapper) {
        this.productsRepo = productsRepo;
        this.storeRepo = storeRepo;
        this.modelMapper = modelMapper;
    }

    public List<ProductDto> getProducts() {
        List<ProductsModel> allproducts = productsRepo.findAll();
        return allproducts.stream()
        .map(product -> modelMapper.map(product, ProductDto.class))
        .toList();
    }

    public List<ProductDto> getProductsForSeller(User user) {
        List<StoreModel> stores = storeRepo.findAllByUser(user);
        //IN passes multiple store model at a time preventing mutiple DB querying which could happen if we use flatMap.
        return productsRepo.findByStoreModelIn(stores)
        .stream()
        .map(product -> modelMapper.map(product, ProductDto.class))
        .toList();
    }
    public ProductDto getProductById(long id) {
        ProductsModel product = productsRepo.findById(id)
        .orElseThrow(()-> new RuntimeException("Product cannot be found"));

        return modelMapper.map(product, ProductDto.class);

    }
    public void saveProducts(AddProductDto productDto, User user) {
        StoreModel store = storeRepo.findByStoreIdAndUser(productDto.getStoreId(), user)
        .orElseThrow(() -> new IllegalArgumentException("Store not found for the given User"));
        ProductsModel existing = productsRepo.findByStoreModelAndCategoryAndColorAndSize(store, productDto.getCategory(), productDto.getColor(), productDto.getSize());

        if (existing!=null) {
            existing.setQuantity(existing.getQuantity() + productDto.getQuantity());
            productsRepo.save(existing);
            return;
        }

        ProductsModel product = new ProductsModel();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setImageURL(productDto.getImageURL());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());
        product.setColor(productDto.getColor());
        product.setSize(productDto.getSize());
        product.setStoreModel(store);
    } 

}
