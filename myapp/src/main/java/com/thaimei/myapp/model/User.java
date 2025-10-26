package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;


import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;

 @Entity
 @Table(name="app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false)
   private String username;

   @Column(nullable = false)
   private String password;

   private String email;
   @Column(nullable = false)
   private String status;

   public User() {
   }
   public User(String username, String password,String email) {
    this.username =username;
    this.password = password;
    this.email=email;
   }
   @Column(nullable= false)
   private String role= "USER";
   @Column(updatable = false)
   @CreationTimestamp
   private LocalDateTime createdAt;

   public String getRole() {
    return role;
   }

   public void setRole(String role) {
    this.role = role;
   }

   public LocalDateTime getCreatedAt() {
    return createdAt;
   }
   

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
   @OneToOne(mappedBy="user", cascade = CascadeType.ALL)
   private UserprofileModel userprofileModel;

   public UserprofileModel getUserprofileModel() {
    return userprofileModel;
   }
   public void setUserprofileModel(UserprofileModel userprofileModel) {
    this.userprofileModel=userprofileModel;
   }
   public String getStatus() {
    return status;
   }
   public void setStatus(String status) {
    this.status = status;
   }


    
}
