package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.thaimei.myapp.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus= PaymentStatus.PENDING;
    

   @Column(nullable=false)
   private double totalAmount;

   
    
    @Column(nullable = false) 
    private String paymentMethod;
   
    @Column(nullable = false, unique=true)
    private String paymentId; 

    @Column(nullable = false, unique = true)
    private String orderId; 
    
    @Column(nullable = false)
    private String currency="INR";
}
    
