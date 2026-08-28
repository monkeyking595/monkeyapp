package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thaimei.myapp.model.RefundModel;

public interface  RefundRepo extends JpaRepository<RefundModel, Long> {
    
}
