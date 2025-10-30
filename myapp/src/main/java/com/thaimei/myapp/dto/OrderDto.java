package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class OrderDto {
    public OrderDto() {}
    
    public OrderDto(Long id, String productName, int quantity, String status, double totalPrice, String imageURL) {
        this.id=id;
        this.productName=productName;
        this.quantity=quantity;
        this.status=status;
        this.totalPrice=totalPrice;
        this.imageURL=imageURL;
    }
    
    @NotNull(message="ID cannot be null")
    private Long id;
    @NotBlank(message = "Name cannot be empty")
    private String productName;
    @Positive(message="Quantity cannot be negavtive")
    private int quantity;
    @NotBlank(message = "Status cannot be blank")
    private String status;
    @Positive(message = "Total price cannot be negative")
    private double totalPrice;
    @NotBlank(message = "image cannot be blank")
    private String imageURL;
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
        this.totalPrice = totalPrice;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName=productName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
}
