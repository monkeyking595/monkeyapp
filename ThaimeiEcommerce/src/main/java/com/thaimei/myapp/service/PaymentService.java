package com.thaimei.myapp.service;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.Payment;
import com.thaimei.myapp.model.ProcessWebhook;
import com.thaimei.myapp.dto.PaymentDto;
import com.thaimei.myapp.enums.PaymentStatus;

import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.thaimei.myapp.repository.ProcessWebhookRepo;
import com.thaimei.myapp.repository.UserRepository;
import com.thaimei.myapp.repository.OrderRepo;
import com.thaimei.myapp.enums.OrderStatusEnum;
import com.thaimei.myapp.model.Orders;



import com.thaimei.myapp.repository.PaymentRepo;
@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;
    private final ProcessWebhookRepo processWebhookRepo;
    private final UserRepository userRepo;
    private final OrderRepo orderRepo;
    public PaymentService(PaymentRepo paymentRepo, ModelMapper modelMapper, ProcessWebhookRepo processWebhookRepo, UserRepository userRepo, OrderRepo orderRepo) {
        this.paymentRepo=paymentRepo;
        this.modelMapper=modelMapper;
        this.processWebhookRepo=processWebhookRepo;
        this.userRepo=userRepo;
        this.orderRepo=orderRepo;
    }
    public Optional<PaymentDto> getPaymentDetailsByPaymentId(String paymentId) {
        Optional<Payment> paymentDetails =paymentRepo.findByPaymentId(paymentId);
        return paymentDetails.map(checkout-> modelMapper.map(checkout, PaymentDto.class));
    }

    
    public boolean  savePaymentDetails(Object paymentObject) {
        if(paymentObject instanceof PaymentIntent intent) {
            //Map lookup using the key, since metadata is Map
            String orderIdsStr=intent.getMetadata().get("orderIds");
            if(orderIdsStr == null) {
                //write to you application's console/standard output, not ideal for production.
                System.out.println("No orderIds found in metadata for PaymentIntent: " + intent.getId());
                return false;
            }
            //declare the type as String array, split creates the array splitting the ids by comma.
            String[] orderIds = orderIdsStr.split(",");
            String userId = intent.getMetadata().get("userId");
            
            for(String orderId : orderIds) {
                Orders order = orderRepo.findById(Long.valueOf(orderId)).orElse(null);

                if(order == null) {
                    System.out.println("order not found for id:" + orderId);
                    //continue, tells java to immediately stop the current iteration of the loop and jump straight to the next one.
                    continue;
                }

                // this protect against idempotency, if the payment already exists for this orderId and paymentId, we don't want to create a new record, we want to update the existing one.
                Payment payment = paymentRepo.findByPaymentIdAndOrderId(intent.getId(), orderId)
                //else create a new Payment object.
                .orElse(new Payment());

                payment.setUser(userRepo.findById(Long.valueOf(userId)).orElse(null));
                payment.setOrderId(orderId);
                payment.setPaymentId(intent.getId());
                //get the totalprice of each order from the order table and .doubleValue() is a method of bigDecimal which converts it to double.
                payment.setTotalAmount(order.getTotalPrice().doubleValue()); 
                payment.setCurrency(intent.getCurrency().toUpperCase()); // Convert to standard currency code.
                payment.setPaymentStatus(mapStripeStatus(intent.getStatus()));
                // uses a ternary operator to check if the payment method is null, if it is null, it sets it to "UNKNOWN", otherwise it sets it to the actual payment method.
                payment.setPaymentMethod(intent.getPaymentMethod() != null ? intent.getPaymentMethod() : "UNKNOWN");
                paymentRepo.save(payment);

                //if the payment is successful update the order status to confirmed/successful.
                order.setStatus(intent.getStatus().equals("succeeded") ? OrderStatusEnum.CONFIRMED : OrderStatusEnum.FAILED);
                orderRepo.save(order);
            }
            return true;
        }
        else if(paymentObject instanceof Charge charge) {
            //connect back to the parent(paymentIntent), since Charge is created underneath a PaymentIntent, as the record of an actual attempt. so every charge carries a reference to it's parent.
            String intentId = charge.getPaymentIntent();
            if(intentId == null) {
                //could be helful for debugging.
                System.out.println("No paymentIntent linked to refunded charge" + charge.getId());
                return false;
            }

            //find all the rows sharing the current paymentId, since multiple orders could share the same paymentId.
            List <Payment> payments = paymentRepo.findAllByPaymentId(intentId);
            if(payments.isEmpty()) {
                System.out.println("No existing rows found for refund, PaymentmentIntent:" + intentId);
                return false;
            }

            //mark the whole order as refunded for now, no partial refunds, will be integrated later.
            for(Payment payment: payments) {
                payment.setPaymentStatus(PaymentStatus.REFUNDED);
                //updates the existing row not inserting a new row.
                paymentRepo.save(payment);

                //update the order status after the refund
                orderRepo.findById(Long.valueOf(payment.getOrderId())).ifPresent(order ->{
                    order.setStatus(OrderStatusEnum.REFUNDED);
                    orderRepo.save(order);
                });
            }

            return true;
        }
       return false;
    }

    public boolean eventExists(String eventId) {
        return processWebhookRepo.existsById(eventId);
    }

    //this is a  newer Switch expression.
    //map the Stripe payment status to our internal PaymentStatus enum, this is useful for standardizing the status values across different payment providers.
    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch(stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCESSFUL;
            case "failed" -> PaymentStatus.FAILED;
            //the sign "->" here is not a lambda expression, it is a new syntax for switch expressions introduced in Java 12, which allows for more concise and readable code.
            case "refunded" -> PaymentStatus.REFUNDED;
            //default case is used to handle any unexpected or unknown status values, mapping them to PENDING as a safe fallback.
            default -> PaymentStatus.PENDING;
        };
    }

    //save the eventId to the event entity
    public void markEventProcessed(String eventId) {
        //constructor injector, reliable if there are less fields
        processWebhookRepo.save(new ProcessWebhook(eventId, Instant.now()));
    }
    
    
}
