package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.enums.Category;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.ProductStatus;
import com.thaimei.myapp.enums.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;



@Repository
public interface ProductsRepo extends JpaRepository<ProductsModel, Long>  {
    List<ProductsModel> findByName(String name);
    ProductsModel  findByStoreModelAndCategoryAndColorAndSize(StoreModel store, Category category, Color color, Size size);
    Page <ProductsModel> findByStoreModelIn(List<StoreModel> stores, Pageable pageable); 
    //filters the products by it's status (the default is set to ACTIVE)  
    Slice <ProductsModel> findAllByProductStatus( ProductStatus status, Pageable pageable);
    List<ProductsModel> findAllByIdInAndStoreModel_storeId(List<Long> productIds, Long storeId);
}
