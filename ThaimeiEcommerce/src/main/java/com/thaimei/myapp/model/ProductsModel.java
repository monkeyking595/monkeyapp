package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import com.thaimei.myapp.enums.Category;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.Size;   
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint; 
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import com.thaimei.myapp.enums.ProductStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"store_id", "category", "color", "size",}))
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

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreModel storeModel;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Size size;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus productStatus = ProductStatus.ACTIVE;
}


    

