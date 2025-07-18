package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

 @Entity
public class Profile {
@Id    
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String firstname;

private String middlename;

@Column(nullable =false)
private String lastname;

@Column(nullable =false)
private String email;

@Column(nullable = false)
private String phone;

@Column(nullable = false) 
private String gender;

@Column(nullable = false)
private String address;

@Column(nullable=false)
private String city;

@Column(nullable =false) 
private String state;

@Column(nullable = false) 
private String locality;

@Column(nullable=false) 
private String zip;


public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id=id;
}
public String getFirstname() {
    return firstname;
}
public void setFirstname(String firstname) {
    this.firstname= firstname;
}
public String getMiddlename() {
    return middlename;
}
public void setMiddlename(String middlename) {
    this.middlename= middlename;
}
public String getLastname() {
    return lastname;
}
public void setLastname(String lastname) {
    this.lastname=lastname;
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
public String getAddress() {
    return address;
}
public void setAddress(String address) {
    this.address= address;
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
