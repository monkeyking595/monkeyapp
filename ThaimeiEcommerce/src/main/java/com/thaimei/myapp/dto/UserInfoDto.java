package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.Valid;



public class UserInfoDto {
    @NotBlank(message="Name cannot be empty")
    private String fullname;
    @Email(message="Invalid email format")
    private String email;
    @NotBlank(message="Phone cannot be empty")
    @Pattern(regexp="\\d{10}", message="phone must be exactly 10 digits")
    private String phone;
    @NotNull(message="Age cannot be empty")
    @Min(value=1, message="Age must be at least 1")
    @Max(value=120, message="Age must be at most 120")
    private int age;
    @NotBlank(message="Gender cannot be empty")
    private String gender;
    @NotBlank(message="Country cannot be empty")
    private String country;
    @NotBlank(message="City cannot be empty")
    private String city;
    @NotBlank(message="State cannot be empty")
    private String state;
    @NotBlank(message="zip cannot be empty")
    private String zip;
    @NotBlank(message="Locality cannot be empty")
    private String locality;

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country=country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city=city;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String  zip) {
        this.zip=zip;
    }
    public String getState() {
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

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender=gender;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age=age;
    }
    public String  getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone=phone;
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
    public void  setEmail(String email) {
        this.email=email;
    }
    
}
