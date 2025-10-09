package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

 @Entity
public class UserprofileModel {
@OneToOne
@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
private User user;
public User getUser() {
    return user;
}
public void setUser(User user) {
    this.user=user;
}
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

public String getCountry() {
    return country;
}
public void setCountry(String country) {
    this.country=country;
}

public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id=id;
}
public String getFullname() {
    return fullname;
}
public void setFullname(String fullname) {
    this.fullname=fullname;
}
public String getEmail() {
    return email;
}
public void setEmail(String email) {
    this.email=email;
}
public String getPhone() {
    return phone;
}
public void setPhone(String phone) {
    this.phone=phone;
}
public String getGender() {
    return gender;
}
public void setGender(String gender) {
    this.gender= gender;
}

public String getCity() {
    return city;
}
public void setCity(String city) {
    this.city=city;
}
public String  getState() {
    return state;
}
public void setState(String state) {
    this.state=state;
}
public String getLocality() {
    return locality;
}
public void setLocality(String locality) {
    this.locality=locality;
}
public String getZip() {
    return zip;
}
public void setZip(String zip) {
    this.zip=zip;
}
    
}
