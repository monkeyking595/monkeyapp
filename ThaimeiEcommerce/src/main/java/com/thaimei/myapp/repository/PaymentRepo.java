package com.thaimei.myapp.repository;
import com.thaimei.myapp.model.Payment;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository <Payment, Long> {
    public Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findAllByPaymentId(String intentId);
}
