package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
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
    @ManyToOne
    @JoinColumn(name ="user_id", referencedColumnName ="id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "products_id", referencedColumnName="id", nullable=false)
    private Products product;

    public Products getProducts() {
        return product;
    }
    public void setProducts(Products product) {
        this.product=product;

    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id=id;
    }
    public String getProductName() {
        return productName;
    }

    public int  getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL= imageURL;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user=user;
    }

    
}
