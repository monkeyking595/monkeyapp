package com.thaimei.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stripe.model.Charge;
import com.thaimei.myapp.dto.RefundDto;
import com.thaimei.myapp.dto.RefundItemRequestDto;
import com.thaimei.myapp.dto.RefundResponseListDto;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.enums.PaymentStatus;
import com.thaimei.myapp.enums.RefundStatus;
import com.thaimei.myapp.error.AppException;
import com.thaimei.myapp.error.ResourceNotFoundException;
import com.thaimei.myapp.model.OrderItems;
import com.thaimei.myapp.model.Orders;
import com.thaimei.myapp.model.RefundModel;
import com.thaimei.myapp.repository.OrderItemRepo;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.repository.RefundRepo;

import jakarta.transaction.Transactional;



@Service
public class RefundService {
    private final static int REFUND_WINDOWS_DAYS = 7;
    private final RefundRepo refundRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public RefundService(RefundRepo refundRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.refundRepo = refundRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public void requestRefund(RefundDto dto, Long userId) {
        for(RefundItemRequestDto itemreq: dto.getItems()) {
            OrderItems item = orderItemRepo.findById(itemreq.getItemId())
            .orElseThrow(() -> new ResourceNotFoundException ("no item found!" + itemreq.getItemId()));

            Orders order = item.getOrders();
            if(!order.getUser().getId().equals(userId)) {
                throw new AppException("you don't own this order" , 403);
            }

            if(order.getStatus() != OrderStatusEnum.CONFIRMED) {
                throw new AppException("Order is not eligible for refund yet", 400);
            } 

            if (order.getDeliveredAt() == null || LocalDateTime.now().isAfter(order.getDeliveredAt().plusDays(REFUND_WINDOWS_DAYS))) {
                throw new AppException("Refund window has expired", 400);
            }

            boolean alreadyRefunding = refundRepo.findByOrderItem_IdAndStatusIn(item.getId(),List.of(RefundStatus.PENDING, RefundStatus.PROCESSING, RefundStatus.COMPLETED)).isPresent();
            if (alreadyRefunding) {
                throw new AppException("this item has been refunded or being processed", 400);
            }
            BigDecimal amount = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(itemreq.getQuantity()));

            RefundModel record = new RefundModel();
            record.setOrder(order);
            record.setOrderItem(item);
            record.setAmount(amount);
            record.setQuantity(itemreq.getQuantity());
            record.setStatus(RefundStatus.PENDING);
            refundRepo.save(record);
        }
    }

    public void approveRefund(Long id) {

    }

    public RefundResponseListDto getRefunds() {
        
    }

}
