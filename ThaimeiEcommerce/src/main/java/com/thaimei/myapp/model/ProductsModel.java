package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class ProductsModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }


    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private User user;
    public User getUser() {
        return user;
    } 
    public void setUser(User user) {
        this.user=user;
    }

    @Column(nullable= false) 
    private double price;
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
    this.price=price;
}
    @Column(nullable = false) 
    private String description;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description=description;
    }
    @Column(nullable = false) 
    private String imageURL;
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL=imageURL;
    }

    @Column(nullable=false) 
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    @Column(nullable = false) 
    private double quantity;
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}


    

