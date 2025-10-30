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
    public Orders (){}
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

    public ProductsModel getProduct() {
        return product;
    }
    public void setProduct(ProductsModel product) {
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
    public void setProductName(String productName) {
        this.productName=productName;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status=status;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice=totalPrice;
    }

    
}
