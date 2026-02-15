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

   @Column(nullable = false)
   private String email;

   @Column(nullable = false)
   private String status;

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL)
   private UserprofileModel userprofileModel;

   @Column(nullable= false)
   private String role= "USER";
   
   @Column(updatable = false)
   @CreationTimestamp
   private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade =CascadeType.ALL)
    private Cart cart;


}
