package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.thaimei.myapp.enums.OrderStatusEnum;
import jakarta.persistence.Enumerated;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable =false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable= false)
    private String imageURL;

    @Column(nullable=false)
    private String status;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", nullable=false)
    private ProductsModel product;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
 
}
