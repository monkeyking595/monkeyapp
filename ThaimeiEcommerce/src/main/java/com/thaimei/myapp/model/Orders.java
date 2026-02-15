package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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
    private double totalPrice;
    @ManyToOne
    @JoinColumn(name ="user_id", referencedColumnName ="id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", referencedColumnName="id", nullable=false)
    private ProductsModel product;
 
}
