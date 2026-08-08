package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.thaimei.myapp.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;
    
   
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    

    @Column(nullable=false)
    private BigDecimal totalAmount;

    @Column(nullable = false) 
    private String paymentMethod;
   
    @Column(nullable = false, unique = true)
    private String paymentId; 

    
    @Column(nullable = false)
    private String currency="INR";

    @OneToMany(mappedBy ="payment")
    private List<Orders> orders = new ArrayList<>();
}
    
