package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    
    @JoinColumn(name ="user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User user;
    
    @Column(nullable = false)
    private String paymentStatus="PENDING";
    

   @Column(nullable=false)
   private double totalAmount;

   
    
    @Column(nullable = false) 
    private String paymentMethod;
   
    @Column(nullable = false, unique=true)
    private String paymentId; //razorpay payment id

    @Column(nullable = false, unique = true)
    private String orderId; //razorpay orderId
    @Column(nullable = false)
    private String currency="INR";
}
    
