package com.thaimei.myapp.service;
import com.thaimei.myapp.dto.ProductDto;
import com.thaimei.myapp.dto.sellersDto.AddProductDto;
import com.thaimei.myapp.enums.ProductStatus;
import  com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import com.thaimei.myapp.repository.ProductsRepo;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.User;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.repository.StoreRepo;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Pageable;


import com.thaimei.myapp.error.AppException;
import com.thaimei.myapp.error.ResourceNotFoundException;
import org.springframework.lang.NonNull;




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

    //the product listing page for customers will be filter by status (set to active by default) so any status rather than active will be hidden from the users
    public Slice<ProductDto> getProducts(@NonNull Pageable pageable, ProductStatus status) {
        Slice<ProductsModel> allproducts = productsRepo.findAllByProductStatus(status, pageable);
        return allproducts
        .map(product -> modelMapper.map(product, ProductDto.class));
    }

    public Slice<ProductDto> getProductsForSeller(User user, Pageable pageable) {
        List<StoreModel> stores = storeRepo.findAllByUser(user);
        //In passes multiple store model at a time preventing mutiple DB querying which could happen if we use flatMap.
        //getContent() = method of Page which returns the actual content of the Page in List<T> since we can't store the storeModel as Page<T>
        return productsRepo.findByStoreModelIn(stores, pageable)
        .map(product -> modelMapper.map(product, ProductDto.class));
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
        productsRepo.save(product);
    } 

    public void deleteProducts (Long storeId, List<Long> productIds, Long userId) {
        StoreModel store = storeRepo.findById(storeId)
        .orElseThrow(() -> new ResourceNotFoundException("store not found"));

        if(!store.getUser().getId().equals(userId)) {
            throw new AppException("you don't own this store", 403);
        }

        List<ProductsModel> products = productsRepo.findAllById(productIds);
        if(products.stream().anyMatch(p -> !p.getStoreModel().getStoreId().equals(storeId))) {
            throw new AppException("some products don't belong to this store",403);
        }
        productsRepo.deleteAllById(productIds);

    }

    //diabling the products, it's a bulk request which takes a list of ids.
    public void updateProductsStatus(Long storeId, List<Long> productIds, ProductStatus status) {
        List<ProductsModel> products = productsRepo.findAllByIdInAndStoreModel_storeId(productIds, storeId);
        //forEach() this is a method of List/Collection, it can be call on any iterable object, it loops through every element in the collection and runs whatever code you give it against each one.
        products.forEach(p-> p.setProductStatus(status));
        productsRepo.saveAll(products);
    }

}
