package com.thaimei.myapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.thaimei.myapp.model.StoreModel;
import com.thaimei.myapp.model.User;
import java.util.Optional;
import java.util.List;


@Repository
public interface StoreRepo extends JpaRepository<StoreModel, Long> {
    List<StoreModel> findAllByUser(User user);
    Optional<StoreModel> findByStoreIdAndUser(Long id, User user);
}
    

