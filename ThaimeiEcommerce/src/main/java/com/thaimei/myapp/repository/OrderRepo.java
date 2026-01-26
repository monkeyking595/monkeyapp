package com.thaimei.myapp.repository;
import java.util.List;
import com.thaimei.myapp.model.Orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    public List<Orders>  findByUserId (Long id);


    
}
