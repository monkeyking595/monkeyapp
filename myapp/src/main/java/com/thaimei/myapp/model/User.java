package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

 @Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false)
   private String username;

   @Column(nullable = false)
   private String password;

   private String email;

   public Long getId() {
    return id;
   }
   public void setId(Long id) {
    this.id=id;
   }
   public String getUsername() {
    return username;
   }
   public void setUsername(String username) {
    this.username = username;
   }
   public String getPassword() {
    return password;
   }
   public void setPassword(String password) {
    this.password = password;
   }
   public String getEmail() {
    return email;
   }
   public void setEmail(String email) {
    this.email = email;
   }


    
}
