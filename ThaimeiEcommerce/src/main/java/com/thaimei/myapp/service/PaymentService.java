package com.thaimei.myapp.service;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.Payment;
import com.thaimei.myapp.dto.PaymentDto;
import com.thaimei.myapp.enums.PaymentStatus;

import org.modelmapper.ModelMapper;
import java.util.Optional;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.thaimei.myapp.repository.ProcessWebhookRepo;
import com.thaimei.myapp.repository.UserRepository;



import com.thaimei.myapp.repository.PaymentRepo;
@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;
    private final ProcessWebhookRepo processWebhookRepo;
    private final UserRepository userRepo;
    public PaymentService(PaymentRepo paymentRepo, ModelMapper modelMapper, ProcessWebhookRepo processWebhookRepo, UserRepository userRepo) {
        this.paymentRepo=paymentRepo;
        this.modelMapper=modelMapper;
        this.processWebhookRepo=processWebhookRepo;
        this.userRepo=userRepo;
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
            //decalre the type as String array, split creates the array splitting the ids by comma.
            String[] orderIds = orderIdsStr.split(",");
            String userId = intent.getMetadata().get("userId");

            for(String orderId : orderIds) {
                // this protect against idempotency, if the payment already exists for this orderId and paymentId, we don't want to create a new record, we want to update the existing one.
                Payment payment = paymentRepo.findByPaymentIdAndOrderId(intent.getId(), orderId)
                //else create a new Payment object.
                .orElse(new Payment());
                payment.setUser(userRepo.findById(Long.valueOf(userId)).orElse(null));
                payment.setOrderId(orderId);
                payment.setPaymentId(intent.getId());
                //converts the amount from cents to dollars (or the main currency unit) by dividing by 100.0, since Stripe sends amounts in the smallest currency unit.
                payment.setTotalAmount(intent.getAmount()/100.0); 
                payment.setCurrency(intent.getCurrency().toUpperCase()); // Convert to standard currency code.
                payment.setPaymentStatus(mapStripeStatus(intent.getStatus()));
                // uses a ternary operator to check if the payment method is null, if it is null, it sets it to "UNKNOWN", otherwise it sets it to the actual payment method.
                payment.setPaymentMethod(intent.getPaymentMethod() != null ? intent.getPaymentMethod() : "UNKNOWN");
                paymentRepo.save(payment);
            }
        }
        else if(paymentObject instanceof Charge charge) {
            payment.setPaymentId(charge.getId());
            payment.setTotalAmount(charge.getAmount()/100.0);
            payment.setCurrency(charge.getCurrency().toUpperCase());
            payment.setPaymentStatus(charge.getStatus().toUpperCase());
            payment.setPaymentMethod(charge.getPaymentMethod());
        }
        if(payment.getPaymentId()!=null) {
            paymentRepo.save(payment);
            System.out.println("Payment details saved for ID: " + payment.getPaymentId());
            return true;
        }
        else {
            System.out.println("Payment ID is null, cannot save payment details.");
            return false;
        }

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

    
    
}
