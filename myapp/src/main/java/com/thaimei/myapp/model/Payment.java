package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    @JoinColumn(name ="user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user=user;
    }

    @Column(nullable =false) 
    private double price;
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
    @Column(nullable = false) 
    private String imageURL;
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL=imageURL;
    }

    @Column(nullable = false) 
    private String productName;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName=productName;
    }

    @Column(nullable = false) 
    private int quantity;
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

   
    public double getTotalAmount()  {
        return price *quantity;
    }
    

    @Column(nullable = false) 
    private String paymentMethod;
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod=paymentMethod;
    }
    }
