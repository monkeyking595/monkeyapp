package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thaimei.myapp.model.ProductsModel;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepo extends JpaRepository<ProductsModel, Long>  {
    List<ProductsModel> findByName(String name);
}
