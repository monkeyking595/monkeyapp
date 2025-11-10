package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.Payment;
import com.thaimei.myapp.dto.PaymentDto;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository <Payment, Long> {
    public Optional<Payment> findByPaymentId(String paymentId);
}
