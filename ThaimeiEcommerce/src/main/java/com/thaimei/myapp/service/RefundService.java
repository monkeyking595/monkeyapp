package com.thaimei.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import com.thaimei.myapp.dto.RefundDto;
import com.thaimei.myapp.dto.RefundItemRequestDto;
import com.thaimei.myapp.dto.RefundResponseListDto;
import com.thaimei.myapp.dto.ResponseRefundDto;
import com.thaimei.myapp.dto.UserSideRefundDto;
import com.thaimei.myapp.dto.UserSideResponseRefundDto;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.enums.RefundStatus;
import com.thaimei.myapp.error.AppException;
import com.thaimei.myapp.error.ResourceNotFoundException;
import com.thaimei.myapp.model.OrderItems;
import com.thaimei.myapp.model.Orders;
import com.thaimei.myapp.model.RefundModel;
import com.thaimei.myapp.repository.OrderItemRepo;
import com.thaimei.myapp.repository.RefundRepo;
import com.thaimei.myapp.model.Payment;

import jakarta.transaction.Transactional;



@Service
public class RefundService {
    private final static int REFUND_WINDOWS_DAYS = 7;
    private final RefundRepo refundRepo;
    private final OrderItemRepo orderItemRepo;

    public RefundService(RefundRepo refundRepo, OrderItemRepo orderItemRepo) {
        this.refundRepo = refundRepo;
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

    @Transactional
    public void approveRefund(Long id) {
        RefundModel refund = refundRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException ("refund Request not found" + id));

        if(refund.getStatus() != RefundStatus.PENDING) {
            throw new AppException("only pending refunds request can be approved", 400);
        }

        Orders order = refund.getOrder();
        Payment payment = order.getPayment();

        if(payment ==  null) {
            throw new AppException("Payment not found for this order", 400);
        }

        try {
            RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(payment.getPaymentId())
                .setAmount(refund.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
                .putMetadata("refundRecord", refund.getId().toString())
                .build();

                 Refund.create(params);

                refund.setStatus(RefundStatus.PROCESSING);
                refundRepo.save(refund);
        } catch (StripeException e) {
            refund.setStatus(RefundStatus.FAILED);
            refundRepo.save(refund);
            throw new AppException("Failed to process refund" + e.getMessage(), 502);
        }
    }

    public RefundResponseListDto getRefunds() {
        List<RefundModel> pendingRefunds = refundRepo.findByStatus(RefundStatus.PENDING);

        List<ResponseRefundDto> dtos = pendingRefunds.stream()
        .map(refund -> new ResponseRefundDto(refund.getId(), 
        refund.getStatus(), 
        refund.getOrderItem().getProduct().getName(),
        refund.getQuantity(),
        refund.getAmount(),
        refund.getCreatedAt()
    ))
        .toList();
        RefundResponseListDto response = new RefundResponseListDto();
        response.setRefundResponseList(dtos);
        return response;
    }

    @Transactional 
    public void completeRefund(Long refundRecordId) {
        RefundModel refund = refundRepo.findById(refundRecordId)
        .orElseThrow(() -> new ResourceNotFoundException("Refund record not found for this Id" + refundRecordId));
        refund.setStatus(RefundStatus.COMPLETED);
        refundRepo.save(refund);
    }

    public UserSideResponseRefundDto getRefundForUser(Long userId) {
        List<RefundModel> refunds = refundRepo.findByOrder_User_Id(userId);
        List<UserSideRefundDto> dtos = refunds.stream()
        .map(refund -> new UserSideRefundDto(
            refund.getId(),
            refund.getOrderItem().getProduct().getName(),
            refund.getQuantity(),
            refund.getAmount(),
            refund.getStatus(),
            refund.getCreatedAt()
        ))
        .toList();

        UserSideResponseRefundDto response = new UserSideResponseRefundDto();
        response.setRefunds(dtos);
        return response;
    }

}
