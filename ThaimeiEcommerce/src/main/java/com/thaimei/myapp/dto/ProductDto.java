package com.thaimei.myapp.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductDto {
    public ProductDto(Long id, String name, double price, String description, String imageURL, double quantity) {
        this.id=id;
        this.name=name;
        this.price=price;
        this.description=description;
        this.imageURL=imageURL;
        this.quantity=quantity;
    }
    @NotNull(message ="Id cannot be null")
    private Long id;
    @NotBlank(message ="Product name cannot be blank")
    private String name;
    @Positive(message="price cannot be negative")
    private double price;
    @NotBlank(message="description cannot be blank")
    private String description;
    @NotBlank(message="Image cannot be blank")
    private String imageURL;
    @Positive(message="Quantity cannot be negative")
    private double quantity;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name=name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price=price;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description=description;
    }

    
}
