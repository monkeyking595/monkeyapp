package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.thaimei.myapp.enums.BusinessType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import com.thaimei.myapp.enums.StoreStatus;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StoreModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    @OneToMany(mappedBy="storeModel", cascade = CascadeType.ALL)
    private List<ProductsModel> productsModel;

    @Enumerated(EnumType.STRING) 
    private StoreStatus storeStatus;

    // coordinates will be hardcoded for now later geocoding will be implemented
    private Double longitude;
    
    private Double latitude;
}
