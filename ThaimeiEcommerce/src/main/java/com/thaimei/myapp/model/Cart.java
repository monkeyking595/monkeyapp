package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cart {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable= false)
    private User user;

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    
   
    public Long getId() {
        return id;
    }
     public void setId(Long id) {
        this.id =id;
     }

     public User getUser() {
        return user;
     }
     public void setUser(User user) {
        this.user=user;
     }
}

