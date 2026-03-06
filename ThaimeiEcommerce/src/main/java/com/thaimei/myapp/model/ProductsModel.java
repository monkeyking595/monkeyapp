package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsModel {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long productId;

    @Column(nullable= false) 
    private BigDecimal price;
    

    @Column(nullable = false) 
    private String description;
    
    @Column(nullable = false) 
    private String imageURL;
    

    @Column(nullable=false) 
    private String name;
   
    @Column(nullable = false) 
    private int quantity;
}


    

