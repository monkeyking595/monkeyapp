package com.thaimei.myapp.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import java.time.LocalDateTime;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;  
import com.thaimei.myapp.enums.RoleEnum;
import jakarta.persistence.OneToMany;
import java.util.List;
import com.thaimei.myapp.enums.UserStatus;

@Entity
@Table(name="app_user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

   @Column(nullable = false)
   private String username;

   @Column(nullable = false)
   private String password;

   @Column(unique = true, nullable = false)
   private String email;

   //set the status value to "ACTIVE" by default the Non-nullable constraint ensures that the status field must always have a value, and it cannot be null in the database. This means that when a new User entity is created, if no value is provided for the status field, it will automatically be set to "ACTIVE".
   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private UserStatus status= UserStatus.ACTIVE;

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL)
   private UserprofileModel userprofileModel;

   @Column(nullable= false)
   @Enumerated(EnumType.STRING)
   private RoleEnum role;
   
   @Column(updatable = false)
   //automatically gets the current time.
   //LocalDateTime a class that represents Date and time together
   @CreationTimestamp
   private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade =CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade= CascadeType.ALL)
    private List<StoreModel> storeModel;
}
