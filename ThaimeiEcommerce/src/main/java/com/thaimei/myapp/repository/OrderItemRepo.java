package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaimei.myapp.model.OrderItems;

public interface OrderItemRepo extends JpaRepository<OrderItems, Long> {
    
} 
    

