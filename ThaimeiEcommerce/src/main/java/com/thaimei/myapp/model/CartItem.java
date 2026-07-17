package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue (strategy= GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long itemId;

    //many cartItems can belong to one product meaning one product can be in many cartItems of multiple users/different carts
    //but there can be only one product in one cartItems no duplicates, only the quantity increases.
    //one product = one cartItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductsModel product;

    //LAZY fetch type is used to avoid loading the entire cart when fetching a cartItem, which can be inefficient if the cart has many items. Instead, the cart is only loaded when explicitly accessed.
    //EAGER fetch type is used to load the associated product when fetching a cartItem,
    //Lazy fetch could prevent N+1 query problem, which is wasteful and can lead to performance issues when fetching a list of cartItems, as it would require additional queries to fetch the associated products for each cartItem.
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;


    @Column(nullable = false)
    private int quantity;
}
