package com.thaimei.myapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thaimei.myapp.model.StoreModel;

@Repository
public interface BrandRepo extends JpaRepository<StoreModel, Long> {

}
    

