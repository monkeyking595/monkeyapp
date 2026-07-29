package com.thaimei.myapp.dto;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.Category;
import com.thaimei.myapp.enums.Color;
import com.thaimei.myapp.enums.ProductStatus;
import com.thaimei.myapp.enums.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    
    private Long productId;
 
    private String name;
    
    private BigDecimal price;
   
    private String description;
   
    private String imageURL;
   
    private int quantity;
    
    private Category category;
   
    private Color color;
  
    private Size size;

    private ProductStatus status;
    
}
