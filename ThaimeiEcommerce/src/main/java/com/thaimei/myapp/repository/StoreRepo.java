package com.thaimei.myapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thaimei.myapp.model.StoreModel;
import java.util.Optional;
import com.thaimei.myapp.model.User;

@Repository
public interface StoreRepo extends JpaRepository<StoreModel, Long> {
    Optional<StoreModel> findByUser(User user);
}
    

