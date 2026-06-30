package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import com.thaimei.myapp.enums.OrderStatusEnum;
import jakarta.persistence.Enumerated;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private OrderStatusEnum status = OrderStatusEnum.PENDING;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreModel store;

    //cascade = whatever operation you do on the parent, it cascades down to the children automatically.
    //operations like delete, save, update (cascade means flows down in general)
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creationTime;

}
