package com.thaimei.myapp.repository;
import java.util.List;
import com.thaimei.myapp.model.Orders;
import org.springframework.data.domain.Pageable;
import com.thaimei.myapp.model.User;
import org.springframework.data.domain.Slice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    public List<Orders>  findByUserId (Long id);
    Slice<Orders> findByStore_User(User user, Pageable pageable);
    Slice<Orders> findByStoreId(Long storeId, Pageable pageable);
}
