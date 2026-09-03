package com.thaimei.myapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thaimei.myapp.enums.RefundStatus;
import com.thaimei.myapp.model.RefundModel;

public interface  RefundRepo extends JpaRepository<RefundModel, Long> {
    Optional<RefundModel> findByOrderItem_IdAndStatusIn(Long itemId, List<RefundStatus> statuses);
}
