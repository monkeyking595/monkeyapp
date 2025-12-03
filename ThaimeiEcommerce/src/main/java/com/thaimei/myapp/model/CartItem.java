package com.thaimei.myapp.model;
import javax.persistence.Entity;

import com.thaimei.myapp.model.ProductsModel;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


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
    private double price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String productName;
   
    
}
