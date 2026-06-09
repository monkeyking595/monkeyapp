package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.enums.Category;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Repository
public interface ProductsRepo extends JpaRepository<ProductsModel, Long>  {
    List<ProductsModel> findByName(String name);
    ProductsModel  findByStoreModelAndCategoryAndColorAndSize(StoreModel store, Category category, Color color, Size size);
    Page <ProductsModel> findByStoreModelIn(List<StoreModel> stores, Pageable pageable);   
}
