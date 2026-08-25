package com.thaimei.myapp.service;

import org.springframework.stereotype.Service;

import com.stripe.model.Charge;
import com.thaimei.myapp.model.Payment;
import com.thaimei.myapp.dto.RefundDto;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.enums.PaymentStatus;
import com.thaimei.myapp.model.Orders;
import com.thaimei.myapp.repository.PaymentRepo;

@Service
public class RefundService {
    private final PaymentRepo paymentRepo;

    publib RefundService(PaymentRefund paymentRefund) {
        this.paymentRefund = paymentRefund;
    }
    
    public boolean  recordRefund(RefundDto dto, Long userId) {

        if(paymentObject instanceof Charge charge) {
            //connect back to the parent(paymentIntent), since Charge is created underneath a PaymentIntent, as the record of an actual attempt. so every charge carries a reference to it's parent.
            String intentId = charge.getPaymentIntent();
            if(intentId == null) {
                //could be helful for debugging.
                System.out.println("No paymentIntent linked to refunded charge" + charge.getId());
                return false;
            }

            //find all the rows sharing the current paymentId, since multiple orders could share the same paymentId.
            Payment payment = paymentRepo.findByPaymentId(intentId).orElse(null);
            if(payment==null) {
                System.out.println("No existing row found for refund, PaymentmentIntent:" + intentId);
                return false;
            }

            //mark the whole order as refunded for now, no partial refunds, will be integrated later.
                payment.setPaymentStatus(PaymentStatus.REFUNDED);
                //updates the existing row not inserting a new row.
                paymentRepo.save(payment);

                //update the order status after the refund
                for(Orders order: payment.getOrders()) {
                    order.setStatus(OrderStatusEnum.REFUNDED);
                    orderRepo.save(order);
                }
            return true;
        }

    }
      
    
}
