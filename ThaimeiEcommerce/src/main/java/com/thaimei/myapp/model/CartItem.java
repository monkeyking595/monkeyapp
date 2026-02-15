package com.thaimei.myapp.model;
import jakarta.persistence.Entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;

@Entity
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long itemId;

     @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "productId", nullable = false)
    private ProductsModel product;
    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "cartId", nullable = false)
    private Cart cart;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private int Quantity;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String description;

    




   
    
}
