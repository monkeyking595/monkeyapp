package com.thaimei.myapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stripe.model.Charge;
import com.thaimei.myapp.dto.RefundDto;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.enums.PaymentStatus;
import com.thaimei.myapp.model.Orders;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.repository.RefundRepo;

import jakarta.transaction.Transactional;

import com.thaimei.myapp.dto.ItemRequestDto;

@Service
public class RefundService {
    private final RefundRepo refundRepo;
    private final OrderRepo orderRepo;

    public RefundService(RefundRepo refundRepo, OrderRepo orderRepo) {
        this.refundRepo = refundRepo;
        this.orderRepo = orderRepo;
    }

}
