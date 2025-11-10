package com.thaimei.myapp.service;
import org.springframework.stereotype.Service;
import com.thaimei.myapp.model.Payment;
import com.thaimei.myapp.dto.PaymentDto;
import org.modelmapper.ModelMapper;
import java.util.Optional;


import com.thaimei.myapp.repository.PaymentRepo;
@Service
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;
    public PaymentService(PaymentRepo paymentRepo, ModelMapper modelMapper) {
        this.paymentRepo=paymentRepo;
        this.modelMapper=modelMapper;
    }
    public Optional<PaymentDto> getPaymentDetailsByPaymentId(String paymentId) {
        Optional<Payment> payment=paymentRepo.findPaymentById(paymentId);
        return payment.map(checkout-> modelMapper.map(checkout, PaymentDto.class));
    }
    public boolean  savePaymentDetails(Object paymentObject) {
        Payment payment=new Payment();
        if(paymentObject instanceof PaymentIntent intent) {
            payment.setPaymentId(intent.getId());
            payment.setTotalAmount(intent.getAmount()/100.0); // Stripe sends amount in the smallest unit (cents).
            payment.setCurrency(intent.getCurrency().toUpperCase()); // Convert to standard currency code.
            payment.setPaymentStatus(intent.getStatus().toUpperCase());
            payment.setPaymentMethod(intent.getPaymentMethodTypes().isEmpty() ? "UNKNOWN" : intent.getPaymentMethodTypes().get(0));
        }
        else if(paymentObject instanceof Charge charge) {
            payment.setPaymentId(charge.getId());
            payment.setTotalAmount(charge.getAmount()/100.0);
            payment.setCurrency(charge.getCurrency().toUpperCase());
            payment.setStatus(charge.getStatus().toUpperCase());
            payment.setPaymentMethod(charge.getPaymentMethodTypes());
        }
        if(payment.getPaymentId()!=null) {
            paymentRepo.save(payment);
            System.out.println("Payment details saved for ID: " + payment.getPaymentId());
        }
        else {
            System.out.println("Payment ID is null, cannot save payment details.");
        }

    }

    
    
}
