package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

 @Entity
    @Getter
    @Setter
    @NoArgsConstructor
public class UserprofileModel {
@OneToOne
@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
private User user;
@Id    
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String fullname;


@Column(nullable =false)
private String email;

@Column(nullable = false)
private String phone;

@Column(nullable = false) 
private String gender;


@Column(nullable=false)
private String city;

@Column(nullable =false) 
private String state;

@Column(nullable = false) 
private String locality;

@Column(nullable=false) 
private String zip;


@Column(nullable = false)
private String country;

}